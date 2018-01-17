// !WITH_NEW_INFERENCE

fun <T> materialize(): T = TODO()

val a: () -> Unit = l@{
    // Expected type 'Unit' is used here for inference
    if (true) return@l materialize()

    // Expected type here is Unit, but it also implies coercion,
    // so we can end lambda body with statement
    if (true) <!UNUSED_EXPRESSION!>42<!>
}

val b: () -> Unit = l@{
    // Error, coercion can't be applied at this position!
    if (true) return@l <!TYPE_MISMATCH!>"hello"<!>

    // However, this is OK, because here coercion is applied
    <!UNUSED_EXPRESSION!>"hello"<!>
}

val c: () -> Unit = {
    // Interesting enough, for such expessions we use expected type Unit
    // (compare that with the previous case, where we didn't used expected type Unit for "hello")
    materialize()
}


fun d(): Unit = run {
    run {
        // Ok, block is coerced, because it has Unit-expected type
        "hello"
    }
}

fun e(): Unit = run {
    // Ok, expected type is applied
    materialize()
}

fun f(): Unit = run {
    run {
        // Attention!
        // In OI expected type 'Unit' isn't applied here because of implementation quirks
        // In NI, it is applied and call is correctly inferred
        <!OI;TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>materialize<!>()
    }
}