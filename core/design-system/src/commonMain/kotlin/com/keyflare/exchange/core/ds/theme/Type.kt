package com.keyflare.exchange.core.ds.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.keyflare.exchange.core.ds.common.CustomTypography
import exchange.core.design_system.generated.resources.Res
import exchange.core.design_system.generated.resources.inter_500_medium
import exchange.core.design_system.generated.resources.inter_600_semibold
import exchange.core.design_system.generated.resources.inter_700_bold
import exchange.core.design_system.generated.resources.jetbrains_mono_500_medium
import exchange.core.design_system.generated.resources.jetbrains_mono_600_semibold
import exchange.core.design_system.generated.resources.jetbrains_mono_700_bold
import org.jetbrains.compose.resources.Font

expect val platformTextStyle: PlatformTextStyle?

@Composable
fun type(): CustomTypography {
    val interFamily = FontFamily(
        Font(Res.font.inter_500_medium, FontWeight.Medium),
        Font(Res.font.inter_600_semibold, FontWeight.SemiBold),
        Font(Res.font.inter_700_bold, FontWeight.Bold),
    )
    val jetBrainsMonoFamily = FontFamily(
        Font(Res.font.jetbrains_mono_500_medium, FontWeight.Medium),
        Font(Res.font.jetbrains_mono_600_semibold, FontWeight.SemiBold),
        Font(Res.font.jetbrains_mono_700_bold, FontWeight.Bold),
    )

    return CustomTypography(
        digitL = typographyStyle(interFamily, 40, FontWeight.Medium, 0.0),
        digitM = typographyStyle(interFamily, 36, FontWeight.Medium, 0.0),
        digitS = typographyStyle(interFamily, 24, FontWeight.Medium, 0.0),
        heading1 = typographyStyle(interFamily, 20, FontWeight.SemiBold, 0.0),
        heading2 = typographyStyle(interFamily, 16, FontWeight.Bold, 0.0),
        heading3 = typographyStyle(interFamily, 16, FontWeight.SemiBold, -0.25),
        heading4 = typographyStyle(interFamily, 14, FontWeight.SemiBold, -0.25),
        body1 = typographyStyle(interFamily, 16, FontWeight.Medium, -0.25),
        body2 = typographyStyle(interFamily, 14, FontWeight.Medium, -0.25),
        body3 = typographyStyle(interFamily, 12, FontWeight.Medium, -0.25),
        body4 = typographyStyle(interFamily, 8, FontWeight.Medium, -0.25),
        digitLMono = typographyStyle(jetBrainsMonoFamily, 40, FontWeight.Medium, 0.0),
        digitMMono = typographyStyle(jetBrainsMonoFamily, 36, FontWeight.Medium, 0.0),
        digitSMono = typographyStyle(jetBrainsMonoFamily, 24, FontWeight.Medium, 0.0),
        heading1Mono = typographyStyle(jetBrainsMonoFamily, 20, FontWeight.SemiBold, 0.0),
        heading2Mono = typographyStyle(jetBrainsMonoFamily, 16, FontWeight.Bold, 0.0),
        heading3Mono = typographyStyle(jetBrainsMonoFamily, 16, FontWeight.SemiBold, -0.25),
        heading4Mono = typographyStyle(jetBrainsMonoFamily, 14, FontWeight.SemiBold, -0.25),
        body1Mono = typographyStyle(jetBrainsMonoFamily, 16, FontWeight.Medium, -0.25),
        body2Mono = typographyStyle(jetBrainsMonoFamily, 14, FontWeight.Medium, -0.25),
        body3Mono = typographyStyle(jetBrainsMonoFamily, 12, FontWeight.Medium, -0.25),
        body4Mono = typographyStyle(jetBrainsMonoFamily, 8, FontWeight.Medium, -0.25),
    )
}

private fun typographyStyle(
    fontFamily: FontFamily,
    fontSize: Int,
    fontWeight: FontWeight,
    letterSpacing: Double,
): TextStyle = TextStyle(
    fontFamily = fontFamily,
    fontSize = fontSize.sp,
    fontWeight = fontWeight,
    letterSpacing = letterSpacing.sp,
    platformStyle = platformTextStyle,
)
