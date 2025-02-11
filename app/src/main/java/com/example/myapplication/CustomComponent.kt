package com.example.myapplication

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CustomComponent(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 0,
    maxIndicatorValue: Int = 1000,
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    indicatorStrokeCup:StrokeCap = StrokeCap.Round,
    foreIngIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    foreIndicatorStrokeWidth: Float = 100f,
    bigTextFontSize:TextUnit = MaterialTheme.typography.headlineMedium.fontSize,
    bigTextColor:Color = MaterialTheme.colorScheme.onSurface,
    bigTextSuffix:String = "GB",
    smallText:String = "Remaining",
    smallTextFontSize:TextUnit = MaterialTheme.typography.headlineLarge.fontSize,
    smallTextColor:Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3F)
) {

    var allowedIndicatorValue by remember {
        mutableStateOf(maxIndicatorValue)
    }

    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue){
        indicatorValue
    }else{
        maxIndicatorValue
    }

    var animatedIndicatorValue by remember { mutableStateOf(0F) }

    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }

    val percentage = (animatedIndicatorValue / maxIndicatorValue) * 100

    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(1000)
    )

    val receivedValue by animateIntAsState(targetValue = allowedIndicatorValue,
        animationSpec = tween(1000)
    )

    val animateBigTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0) {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3F)
        }else bigTextColor,
        animationSpec = tween(1000)
    )

    Column(modifier = Modifier
        .size(canvasSize)
        .drawBehind {
            val componentSize = size / 1.25F
            backgroundIndicator(

                componentSize = componentSize,
                indicatorColor = backgroundIndicatorColor,
                indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
                indicatorStokeCup =indicatorStrokeCup

            )
            foregroundIndicator(
                swipeAngle = sweepAngle,
                componentSize = componentSize,
                indicatorColor = foreIngIndicatorColor,
                indicatorStrokeWidth = foreIndicatorStrokeWidth,
                indicatorStrokeCup = indicatorStrokeCup
            )
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)

    {
        
        EmbeddedElements(
            bigText = receivedValue,
            bigTextTextFontSize = bigTextFontSize,
            bigTextColor = animateBigTextColor,
            bigTextSuffix = bigTextSuffix,
            smallText = smallText,
            smallTextColor = smallTextColor,
            smallTextFontSize = smallTextFontSize
        )

    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
    indicatorStokeCup:StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = indicatorStokeCup
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        ),
        )
}

fun DrawScope.foregroundIndicator(
    swipeAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
    indicatorStrokeCup:StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = swipeAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = indicatorStrokeCup
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        ),


        )
}

@Composable
fun EmbeddedElements(
    bigText:Int,
    bigTextTextFontSize:TextUnit,
    bigTextColor:Color,
    bigTextSuffix:String,
    smallText:String,
    smallTextColor:Color,
    smallTextFontSize:TextUnit
){
    
    Text(
        text = smallText,
        color = smallTextColor,
        fontSize = smallTextFontSize,
        textAlign = TextAlign.Center)
    
    Text(
        text = "$bigText ${bigTextSuffix.take(2)}",
        color = bigTextColor,
        fontSize = bigTextTextFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
        )

}

@Composable
@Preview(showBackground = true)
fun CustomComponentPreview() {
    CustomComponent()
}