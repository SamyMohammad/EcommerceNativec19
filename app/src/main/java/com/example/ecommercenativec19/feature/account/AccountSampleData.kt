package com.example.ecommercenativec19.feature.account

/**
 * DESIGN PLACEHOLDER content copied from the Figma Account frame (node 835:523). There is no account/profile data
 * contract or persistence yet; replace with real data when one exists. The password value is the design's masked
 * placeholder, not a real credential.
 */
internal object AccountSampleData {
    const val WELCOME_NAME = "Jane"
    const val HEADER_EMAIL = "jane.doe@example.com"

    val fields = listOf(
        AccountFieldUi(AccountFieldType.FullName, "Jane Doe"),
        AccountFieldUi(AccountFieldType.Email, "jane.doe@example.com"),
        AccountFieldUi(AccountFieldType.Password, "**********************"),
        AccountFieldUi(AccountFieldType.Mobile, "+20 100 000 0000"),
        AccountFieldUi(AccountFieldType.Address, "123 Example Street, Cairo"),
    )
}
