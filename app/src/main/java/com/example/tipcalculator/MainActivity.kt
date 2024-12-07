package com.example.tipcalculator

import android.os.Bundle
import androidx.compose.material3.TextField
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Surface{
                    TipCalculatorLayout()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TipCalculatorLayoutPreview() {
    TipCalculatorTheme {
        TipCalculatorLayout()
    }
}

@Composable
fun TipCalculatorLayout() {
    var inputAmount by remember { mutableStateOf("") }
    val amount = inputAmount.toDoubleOrNull() ?: 0.0

    var inputTip by remember { mutableStateOf("") }
    val tipPercent = inputTip.toDoubleOrNull() ?: 0.0

    var roundUp by remember { mutableStateOf(false) }

    val tip = calculateTip(roundUp,amount, tipPercent)
     Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Center
     ) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            modifier = Modifier
                .padding( top = 40.dp)
                .align(alignment = Alignment.Start)
        )
         Spacer(modifier = Modifier.height(24.dp))

         EditNumberField(
             value = inputAmount,
             leadingIcon = R.drawable.money,
             onValueChange = { inputAmount = it },
             label = R.string.bill_amount,
             keyboardOptions = KeyboardOptions(
                 keyboardType = KeyboardType.Number,
                 imeAction = ImeAction.Next
             ),
             modifier = Modifier
                 .padding(bottom = 32.dp)
                 .fillMaxWidth()
         )

         EditNumberField(
             label = R.string.how_was_the_service,
             value = inputTip,
             leadingIcon = R.drawable.percent,
             onValueChange = { inputTip = it },
             keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                 imeAction = ImeAction.Done
             ),
             modifier = Modifier
                 .padding(bottom = 32.dp)
                 .fillMaxWidth()
         )
         Spacer(modifier = Modifier.height(24.dp))

         RoundTip(
             roundUp = roundUp,
             onRoundUpChanged = { roundUp = it },
             modifier = Modifier.padding(bottom = 32.dp)
         )

         Text(
             text = stringResource(R.string.tip_amount, tip),
             style = MaterialTheme.typography.displaySmall
         )
         Spacer(modifier = Modifier.height(150.dp))
     }
}
private fun calculateTip(
    roundUp: Boolean,
    amount: Double,
    tipPercent: Double = 0.0
): String{
    var t = tipPercent / 100 * amount
    if(roundUp){
        t = kotlin.math.ceil(t)
    }
    return NumberFormat.getCurrencyInstance().format(t)
}


//This makes EditNumberField stateless. You hoisted the UI state to its ancestor, TipCalculatorLayout(). The TipCalculatorLayout() is the state(amountInput) owner now.
@Composable
fun EditNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes label: Int, //To denote that the label parameter is expected to be a string resource reference, annotate the function parameter with the @StringRes annotation
    keyboardOptions: KeyboardOptions,
    @DrawableRes leadingIcon: Int,
    modifier: Modifier = Modifier
) {
    // yeh hamara state carry karta hai and isse humme pass karna hai upar wale edit text me toh hum karenge state hoisting
    TextField(
        value = value,
        leadingIcon = {Icon(painter = painterResource(id = leadingIcon), contentDescription = null)},
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions,
        modifier = modifier,
    )
}

@Composable
fun RoundTip(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(id = R.string.round_up_tip))
        Switch(
            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
        )
    }
}