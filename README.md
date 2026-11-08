# FoodPanda Seller (Android — Kotlin & Jetpack Compose)

A merchant and restaurant partner management application for **FoodPanda Seller**, engineered with modern Android architecture, **Kotlin 2.2**, **Jetpack Compose (Material 3)**, **Room Database**, and **Gradle Kotlin DSL**.

## Features

- **Merchant Authentication & Shop Onboarding**: Sign in, create a merchant account, or use the quick demo merchant account. Register and configure restaurant details, cuisine tags, preparation times, delivery fees, and approval status.
- **Live Order Management**: Track incoming orders across **Pending**, **Preparing**, **Ready for Pickup**, **Delivering**, and **Completed / Cancelled** states, with rider assignment details, order breakdowns, and status progression actions.
- **Menu, Categories & Customizations**:
  - Create, edit, and reorder menu categories.
  - Add and manage food items with pricing, compare-at discounts, dietary tags, availability toggles, and category assignments.
  - Configure food customization groups (e.g., Spice Level, Add-ons, Portion Size) with required/optional rules and per-choice pricing.
- **Promotional Banners & Vouchers**: Create and toggle promotional store banners and discount vouchers (`%` or `$` off, minimum spend, usage limits).
- **Address & Location Management**: Manage restaurant pickup addresses, coordinates, delivery radius, and interactive address search.
- **Analytics & Order History**: View revenue metrics, completed order history, and filterable transaction records.

## Tech Stack & Engineering

- **UI Framework**: Jetpack Compose with Material 3 (`ColorScheme`, `Poppins` typography, edge-to-edge layout, adaptive navigation drawer & bottom sheets).
- **Architecture**: MVVM (`SellerViewModel`, `StateFlow`, `SellerRepository`) with reactive data streams.
- **Persistence**: Room (`AppDatabase`, `SellerDao`) with KSP annotation processing and automatic initial seed data.
- **Build & Secrets**:
  - Android Gradle Plugin `9.1.1`, Kotlin `2.2.10`, KSP `2.3.5`, Target/Compile SDK `36`, Java `17`.
  - Secrets Gradle Plugin reading from `.env` / `.env.example` (`MAPS_API_KEY`). Configure your API key in the **Secrets panel in AI Studio**.
