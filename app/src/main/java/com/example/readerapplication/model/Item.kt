package com.example.readerapplication.model

data class Item(
    val accessInfo: AccessInfo,
    val etag: String,
    val id: String,
    val kind: String,
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo,
    val selfLink: String,
    val volumeInfo: VolumeInfo
){

companion object{
    fun createExampleItem(): Item {
        // Example AccessInfo
        val accessInfo = AccessInfo(
            country = "US",
            viewability = "PARTIAL",
            embeddable = true,
            publicDomain = false,
            textToSpeechPermission = "ALLOWED",
            accessViewStatus = "SAMPLE",
            epub = Epub(isAvailable = true, acsTokenLink = ""),
            pdf = Pdf(isAvailable = false, acsTokenLink = ""),
            quoteSharingAllowed = true,
            webReaderLink = "http://books.google.com/webreader/bookId123"
        )

        val saleInfo = SaleInfo(
            country = "US",
            saleability = "FOR_SALE",
            isEbook = true,
            buyLink = "http://books.google.com/buy/bookId123",
            listPrice = ListPrice(amount = 29.99, currencyCode = "USD"),
            retailPrice = null,
            offers = listOf(
                Offer(
                    finskyOfferType = 1,
                    listPrice = ListPriceX(amountInMicros = 29, currencyCode = "USD"),
                    retailPrice = RetailPrice(amountInMicros = 24, currencyCode = "USD")
                )
            )
        )

        // Example SearchInfo
        val searchInfo = SearchInfo(
            textSnippet = "This is a short description of the book..."
        )

        // Example VolumeInfo
        val volumeInfo = VolumeInfo(
            title = "Effective Java",
            subtitle = "Best Practices for the Java Platform",
            authors = listOf("Joshua Bloch"),
            publisher = "Addison-Wesley",
            publishedDate = "2018",
            description = "A comprehensive guide to programming in Java...",
            pageCount = 416,
            categories = listOf("Programming", "Software Development"),
            maturityRating = "NOT_MATURE",
            allowAnonLogging = true,
            contentVersion = "1.2.3",
            imageLinks = ImageLinks(
                smallThumbnail = "http://books.google.com/thumbnail/small.jpg",
                thumbnail = "http://books.google.com/thumbnail/large.jpg"
            ),
            industryIdentifiers = listOf(
                IndustryIdentifier(type = "ISBN_13", identifier = "9780134685991"),
                IndustryIdentifier(type = "ISBN_10", identifier = "0134685997")
            ),
            panelizationSummary = PanelizationSummary(
                containsEpubBubbles = false,
                containsImageBubbles = false
            ),
            printType = "BOOK",
            readingModes = ReadingModes(text = true, image = false),
            language = "en",
            previewLink = "http://books.google.com/preview/effective-java",
            infoLink = "http://books.google.com/info/effective-java",
            canonicalVolumeLink = "http://books.google.com/canonical/effective-java"
        )

        return Item(
            accessInfo = accessInfo,
            etag = "etag123",
            id = "bookId123",
            kind = "books#volume",
            saleInfo = saleInfo,
            searchInfo = searchInfo,
            selfLink = "http://books.google.com/item/bookId123",
            volumeInfo = volumeInfo
        )
    }
}
}