package com.example.ecommercenativec19.feature.account

/**
 * DESIGN PLACEHOLDER content copied from the Figma Account frame (node 835:523). There is no account/profile data
 * contract or persistence yet; replace with real data when one exists. The password value is the design's masked
 * placeholder, not a real credential.
 */
internal object AccountSampleData {
    const val WELCOME_NAME = "Mohamed"
    const val HEADER_EMAIL = "mohamed.N@gmail.com"

    val fields = listOf(
        AccountFieldUi(AccountFieldType.FullName, "Mohamed Mohamed Nabil"),
        AccountFieldUi(AccountFieldType.Email, "mohamed.N@gmail.com"),
        AccountFieldUi(AccountFieldType.Password, "**********************"),
        AccountFieldUi(AccountFieldType.Mobile, "01122118855"),
        AccountFieldUi(AccountFieldType.Address, "6th October, street 11....."),
    )
}
