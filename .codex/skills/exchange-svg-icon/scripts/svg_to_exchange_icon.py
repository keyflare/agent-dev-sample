#!/usr/bin/env python3
"""Generate an Exchange ImageVector Kotlin file from a simple SVG."""

from __future__ import annotations

import argparse
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path


DEFAULT_COLOR = "0xFFE8EAED"
PACKAGE_NAME = "com.keyflare.exchange.core.icons.compose.list"


def pascal_case(value: str) -> str:
    parts = re.findall(r"[A-Za-z0-9]+", value)
    if not parts:
        raise ValueError("Icon name must contain at least one letter or digit")
    return "".join(part[:1].upper() + part[1:].lower() for part in parts)


def upper_snake(value: str) -> str:
    parts = re.findall(r"[A-Za-z0-9]+", value)
    if not parts:
        raise ValueError("Icon name must contain at least one letter or digit")
    return "_".join(part.upper() for part in parts)


def lower_camel(value: str) -> str:
    pascal = pascal_case(value)
    return pascal[:1].lower() + pascal[1:]


def strip_namespace(tag: str) -> str:
    return tag.rsplit("}", 1)[-1]


def parse_style(style: str | None) -> dict[str, str]:
    result: dict[str, str] = {}
    if not style:
        return result
    for item in style.split(";"):
        if ":" not in item:
            continue
        key, value = item.split(":", 1)
        result[key.strip()] = value.strip()
    return result


def attr(element: ET.Element, name: str, default: str | None = None) -> str | None:
    style = parse_style(element.attrib.get("style"))
    return element.attrib.get(name) or style.get(name) or default


def parse_float(value: str | None, default: float) -> float:
    if not value:
        return default
    match = re.search(r"-?\d+(?:\.\d+)?", value)
    return float(match.group(0)) if match else default


def parse_viewbox(root: ET.Element) -> tuple[float, float]:
    view_box = root.attrib.get("viewBox")
    if view_box:
        values = [float(value) for value in re.split(r"[,\s]+", view_box.strip()) if value]
        if len(values) == 4:
            return values[2], values[3]

    width = parse_float(root.attrib.get("width"), 24.0)
    height = parse_float(root.attrib.get("height"), 24.0)
    return width, height


def kotlin_float(value: float) -> str:
    if value.is_integer():
        return f"{int(value)}f"
    return f"{value:g}f"


def kotlin_string(value: str) -> str:
    return value.replace("\\", "\\\\").replace('"', '\\"')


def color_expr(value: str | None) -> str:
    if not value or value in {"currentColor", "none"}:
        return DEFAULT_COLOR
    if re.fullmatch(r"#[0-9A-Fa-f]{3}", value):
        r, g, b = value[1], value[2], value[3]
        hex_digits = f"FF{r}{r}{g}{g}{b}{b}".upper()
        return f"0x{hex_digits}"
    if re.fullmatch(r"#[0-9A-Fa-f]{6}", value):
        return f"0xFF{value[1:].upper()}"
    if re.fullmatch(r"#[0-9A-Fa-f]{8}", value):
        return f"0x{value[1:].upper()}"
    return DEFAULT_COLOR


def fill_type(element: ET.Element) -> str:
    rule = attr(element, "fill-rule", "nonzero")
    return "PathFillType.EvenOdd" if rule == "evenodd" else "PathFillType.NonZero"


def stroke_cap(element: ET.Element) -> str:
    value = attr(element, "stroke-linecap", "butt")
    return {
        "round": "StrokeCap.Round",
        "square": "StrokeCap.Square",
    }.get(value or "butt", "StrokeCap.Butt")


def stroke_join(element: ET.Element) -> str:
    value = attr(element, "stroke-linejoin", "miter")
    return {
        "round": "StrokeJoin.Round",
        "bevel": "StrokeJoin.Bevel",
    }.get(value or "miter", "StrokeJoin.Miter")


def path_block(element: ET.Element) -> str:
    data = element.attrib.get("d")
    if not data:
        raise ValueError("Encountered <path> without a d attribute")

    fill = attr(element, "fill")
    stroke = attr(element, "stroke")
    has_stroke = bool(stroke and stroke != "none")
    has_fill = fill != "none"

    fill_expr = f"SolidColor(Color({color_expr(fill)}))" if has_fill else "null"
    stroke_expr = f"SolidColor(Color({color_expr(stroke)}))" if has_stroke else "null"

    return f"""            addPath(
                pathData = PathParser().parsePathString(\"{kotlin_string(data)}\").toNodes(),
                pathFillType = {fill_type(element)},
                fill = {fill_expr},
                fillAlpha = {kotlin_float(parse_float(attr(element, "fill-opacity"), 1.0))},
                stroke = {stroke_expr},
                strokeAlpha = {kotlin_float(parse_float(attr(element, "stroke-opacity"), 1.0))},
                strokeLineWidth = {kotlin_float(parse_float(attr(element, "stroke-width"), 1.0))},
                strokeLineCap = {stroke_cap(element)},
                strokeLineJoin = {stroke_join(element)},
                strokeLineMiter = {kotlin_float(parse_float(attr(element, "stroke-miterlimit"), 4.0))},
            )"""


def generate(svg_path: Path, icon_name: str) -> str:
    root = ET.parse(svg_path).getroot()
    if strip_namespace(root.tag) != "svg":
        raise ValueError("Input file root must be <svg>")

    unsupported = sorted(
        {
            strip_namespace(element.tag)
            for element in root.iter()
            if strip_namespace(element.tag) in {"circle", "ellipse", "line", "polygon", "polyline", "rect"}
        }
    )
    if unsupported:
        raise ValueError(
            "Convert SVG shapes to paths before generating Kotlin. Unsupported elements: "
            + ", ".join(unsupported)
        )

    transformed = [
        strip_namespace(element.tag)
        for element in root.iter()
        if "transform" in element.attrib
    ]
    if transformed:
        raise ValueError("Flatten SVG transforms before generating Kotlin")

    paths = [element for element in root.iter() if strip_namespace(element.tag) == "path"]
    if not paths:
        raise ValueError("SVG must contain at least one <path>")

    viewport_width, viewport_height = parse_viewbox(root)
    enum_name = upper_snake(icon_name)
    property_name = pascal_case(icon_name)
    cache_name = lower_camel(icon_name)
    path_blocks = "\n".join(path_block(element) for element in paths)

    return f"""package {PACKAGE_NAME}

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.icons.AppIcon

val AppIcon.Compose.{property_name}: ImageVector
    get() {{
        if ({cache_name} != null) {{
            return {cache_name}!!
        }}
        {cache_name} = ImageVector.Builder(
            name = AppIcon.{enum_name}.name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = {kotlin_float(viewport_width)},
            viewportHeight = {kotlin_float(viewport_height)},
        ).apply {{
{path_blocks}
        }}.build()
        return {cache_name}!!
    }}

private var {cache_name}: ImageVector? = null
"""


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("svg", type=Path)
    parser.add_argument("--icon-name", required=True, help="Icon name, e.g. receipt-long or RECEIPT_LONG")
    parser.add_argument("--out", type=Path, help="Kotlin output path. Prints to stdout when omitted.")
    args = parser.parse_args()

    try:
        output = generate(args.svg, args.icon_name)
    except Exception as exc:
        print(f"error: {exc}", file=sys.stderr)
        return 1

    if args.out:
        args.out.parent.mkdir(parents=True, exist_ok=True)
        args.out.write_text(output)
    else:
        print(output, end="")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
