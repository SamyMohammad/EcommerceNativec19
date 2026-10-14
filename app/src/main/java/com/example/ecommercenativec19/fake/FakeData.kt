package com.example.ecommercenativec19.fake

import androidx.annotation.DrawableRes
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.feature.account.Profile
import com.example.ecommercenativec19.feature.home.HomeSection
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi

data class FakeCategory(val id: String, val name: String, @param:DrawableRes val imageRes: Int)

data class FakeSubCategory(val id: String, val name: String)

/** Prices are whole EGP. [oldPrice] is set only for discounted products. */
data class FakeProduct(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val oldPrice: Int?,
    val rating: Double,
    val ratingCount: Int,
    val soldCount: Int,
    val subCategoryId: String,
    val images: List<Int>,
)

/** Hardcoded content of the whole app; nothing here is loaded from anywhere. */
object FakeData {
    val categories = listOf(
        FakeCategory("womens_fashion", "Women's Fashion", R.drawable.img_ellipse14_74a31094),
        FakeCategory("beauty", "Beauty & Health", R.drawable.img_ellipse18_6ffae414),
        FakeCategory("mens_fashion", "Men's Fashion", R.drawable.img_ellipse15_bba3a547),
        FakeCategory("music", "Music", R.drawable.img_ellipse19_89f52566),
        FakeCategory("electronics", "Electronics", R.drawable.img_ellipse16_37eb25ff),
        FakeCategory("mobiles", "Mobiles", R.drawable.img_ellipse20_11cc609a),
        FakeCategory("baby_toys", "Baby & Toys", R.drawable.img_ellipse17_185ed824),
        FakeCategory("supermarket", "SuperMarket", R.drawable.img_ellipse21_712ed5ff),
    )

    private val subCategories: Map<String, List<FakeSubCategory>> = mapOf(
        "womens_fashion" to subs("womens_fashion", "Dresses", "Tops", "Skirts", "Shoes", "Bags", "Jewelry"),
        "beauty" to subs("beauty", "Skincare", "Makeup", "Perfume", "Hair care", "Vitamins", "Fitness"),
        "mens_fashion" to subs(
            "mens_fashion", "T-shirts", "Shorts", "Jeans", "Pants", "Footwear", "Suits", "Watches", "Bags", "Eyewear",
        ),
        "music" to subs("music", "Guitars", "Keyboards", "Headphones", "Speakers"),
        "electronics" to subs("electronics", "Laptops", "Cameras", "Tablets", "Smart watches", "Gaming", "TVs"),
        "mobiles" to subs("mobiles", "Smartphones", "Cases", "Chargers", "Accessories"),
        "baby_toys" to subs("baby_toys", "Toys", "Strollers", "Clothes", "Feeding"),
        "supermarket" to subs("supermarket", "Snacks", "Drinks", "Dairy", "Bakery", "Frozen"),
    )

    val products: List<FakeProduct> = listOf(
        product(1, "Nike Air Jordon - Nike shoes flexible for women", 1200, 2000, 4.6, "womens_fashion_shoes", R.drawable.img_frame68_80e9b516),
        product(2, "Tall Cotton Dress", 600, 750, 4.5, "womens_fashion_dresses", R.drawable.img_frame66_4cff3886),
        product(3, "GUESS Women's Handbag", 1200, 1500, 4.8, "womens_fashion_bags", R.drawable.img_frame67_70608138),
        product(4, "Classic Cotton T-shirt", 350, null, 4.2, "mens_fashion_t-shirts", R.drawable.img_frame73_b6c21927),
        product(5, "Slim Fit Blue Jeans", 900, 1300, 4.4, "mens_fashion_jeans", R.drawable.img_frame69_80e90f22),
        product(6, "Leather Sport Watch", 1800, 2100, 3.8, "mens_fashion_watches", R.drawable.img_frame71_25193b09),
        product(7, "Running Shoes Pro", 1500, 2000, 4.8, "mens_fashion_footwear", R.drawable.img_frame70_3f7a88e3),
        product(8, "Wireless Headphones", 1900, 2500, 4.1, "music_headphones", R.drawable.img_frame72_d0f73343),
        product(9, "Smart Watch Series 5", 3500, null, 4.8, "electronics_smart_watches", R.drawable.img_frame65_2ac7cd2d),
        product(10, "Mirrorless Camera Kit", 12000, 14500, 4.7, "electronics_cameras", R.drawable.img_frame9_2ef89760),
        product(11, "Ultrabook Laptop 14\"", 24000, 26000, 4.5, "electronics_laptops", R.drawable.img_frame10_e9353cb3),
        product(12, "Smartphone 128GB", 9500, null, 4.3, "mobiles_smartphones", R.drawable.img_frame11_5082f499),
        product(13, "Fast Charger 65W", 450, 600, 4.0, "mobiles_chargers", R.drawable.img_frame12_d949f599),
        product(14, "Vitamin C Face Serum", 300, 400, 4.6, "beauty_skincare", R.drawable.img_frame13_d6d4d7c2),
        product(15, "Wooden Building Blocks", 250, null, 4.9, "baby_toys_toys", R.drawable.img_frame14_513d4efb),
        product(16, "Organic Olive Oil 1L", 280, 320, 4.4, "supermarket_snacks", R.drawable.img_frame15_18681980),
        product(17, "Acoustic Guitar", 4200, 5000, 4.2, "music_guitars", R.drawable.img_frame17_e0109d22),
        product(18, "Baby Stroller Lite", 3900, null, 3.9, "baby_toys_strollers", R.drawable.img_frame18_eb31deae),
    )

    val profile = Profile(
        name = "Alex Morgan",
        email = "alex.morgan@example.com",
        phone = "01012345678",
        address = "12 Nile Street, Cairo",
    )

    fun subCategoriesOf(categoryId: String): List<FakeSubCategory> = subCategories[categoryId].orEmpty()

    fun product(id: String): FakeProduct? = products.firstOrNull { it.id == id }

    /** Falls back to every product: most sub-categories have none of their own, and an empty list is a dull demo. */
    fun productsIn(subCategoryId: String): List<FakeProduct> =
        products.filter { it.subCategoryId == subCategoryId }.ifEmpty { products }

    fun section(section: HomeSection): List<FakeProduct> = when (section) {
        HomeSection.BestSellers -> products.sortedByDescending { it.soldCount }
        HomeSection.NewArrival -> products.reversed()
        HomeSection.TopRated -> products.sortedByDescending { it.rating }
    }.take(SectionSize)

    private const val SectionSize = 9

    /** Ids are prefixed with the category so equal names ("Bags") in two categories stay distinct. */
    private fun subs(categoryId: String, vararg names: String) =
        names.map { FakeSubCategory(subCategoryId(categoryId, it), it) }

    private fun subCategoryId(categoryId: String, name: String) = "${categoryId}_${name.lowercase().replace(' ', '_')}"

    private fun product(
        number: Int,
        title: String,
        price: Int,
        oldPrice: Int?,
        rating: Double,
        subCategoryId: String,
        @DrawableRes image: Int,
    ) = FakeProduct(
        id = "product-$number",
        title = title,
        description = "$title. Hardcoded sample text for the UI-only build: it only shows how a longer product " +
            "description wraps and scrolls on the details screen.",
        price = price,
        oldPrice = oldPrice,
        rating = rating,
        ratingCount = 500 + number * 137,
        soldCount = 3000 - number * 111,
        subCategoryId = subCategoryId,
        images = listOf(image, R.drawable.img_property1default1_995b0786, image),
    )
}

fun FakeProduct.toCardUi() = ProductCardUi(
    id = id,
    imageRes = images.first(),
    title = title,
    price = price,
    oldPrice = oldPrice,
    rating = rating,
)
