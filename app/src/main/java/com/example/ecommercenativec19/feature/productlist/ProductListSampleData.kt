package com.example.ecommercenativec19.feature.productlist

import com.example.ecommercenativec19.R

/**
 * DESIGN PLACEHOLDER content, copied from the Figma frame "Product list" (726:650) so the screen has something to
 * show. There is no data contract / backend yet; replace this with real data once one exists. Ids are opaque
 * strings that are handed to Product details.
 */
internal object ProductListSampleData {
    private const val Title = "Nike Air Jordon\nNike shoes flexible for wo.."

    val items: List<ProductListItem> = listOf(
        ProductListItem("product-1", R.drawable.img_frame68_80e9b516, Title, "EGP 1,200", "2000 EGP", "4.6"),
        ProductListItem("product-2", R.drawable.img_frame73_b6c21927, Title, "EGP 1,100", "1,500 EGP", "4.5"),
        ProductListItem("product-3", R.drawable.img_frame69_80e90f22, Title, "EGP 1,000", "1,300 EGP", "4.8"),
        ProductListItem("product-4", R.drawable.img_frame71_25193b09, Title, "EGP 1,500", "2000 EGP", "4.8"),
        ProductListItem("product-5", R.drawable.img_frame70_3f7a88e3, Title, "EGP 1,800", "2,100 EGP", "4.2"),
        ProductListItem("product-6", R.drawable.img_frame72_d0f73343, Title, "EGP 1,900", "2,500 EGP", "3.8"),
    )
}
