# 🎨 UI & Theme Updates

## 1. Dark Mode "Black Rectangle" Fix
- **Issue**: In dark mode, a black rectangle appeared below the bottom menu, indicating that the app content wasn't extending behind the system navigation bar.
- **Fix**: Enabled edge-to-edge display in `MainActivity.kt` using `WindowCompat.setDecorFitsSystemWindows(window, false)`. This allows the app content (and our custom background) to draw behind the system bars.

## 2. "Serene Mist" Color Theme
- **Request**: User requested a "more relaxing and calm" tone.
- **New Palette**:
    - **Primary**: `SoftTeal` (`#A0C1B8`) - A cooler, softer eucalyptus green. Replaces the heavier Sage Green.
    - **Secondary**: `SandBeige` (`#E8E4D9`) - A warm, sandy neutral. Replaces the darker Warm Taupe.
    - **Background**: `MistWhite` (`#F5F7F8`) - A very light, airy off-white/gray.
    - **Text**: `SoftCharcoal` (`#374151`) - Softer than pure black for better readability.
    - **Accents**: `OceanBlue` (`#6B8E9B`) and `LeafGreen` (`#86A873`) for a nature-inspired feel.
- **Implementation**: Updated `Color.kt` and `Theme.kt`. Container colors were also lightened to match the new airy aesthetic.

## 📦 Verification
1.  **Check Dark Mode**: Verify that the bottom navigation bar floats over the app background, and there is no solid black bar at the very bottom of the screen.
2.  **Check Theme**: The app should now feel lighter and cooler. The primary green is softer (teal-leaning), and the secondary cards are a lighter sand color.
