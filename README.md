# StyledDialogs for Android

This library makes styling and using dialogs _a piece of cake_.

 ![Screenshot of the dialogs](graphics/screenshot-small.png)

Features:

 - Compatible with Holo style and **Android Design Guidelines**
 - Change style for all dialogs only by changing a **few color resources**
 - Same look for **Android 2.2+**
 - **Same API** as native Android DialogFragments
 - `SimpleDialogFragment` class, which makes displaying simple dialogs a **one line of code**


Manually:

 - clone the project
 - add it as library project in your IDE
 - include latest support library

## How to style all dialogs:

Add following into your application theme:
```xml
<item name="mixtureDialogStyle">@style/CustomDialogStyle</item>
```
```
Define your dialog style, example for light theme:
```xml
<style name="CustomDialogStyle">
	<!-- anything can be left out: -->
	<item name="titleTextColor">@color/dialog_title_text</item>
	<item name="titleSeparatorColor">@color/dialog_title_separator</item>
	<item name="messageTextColor">@color/dialog_message_text</item>
	<item name="buttonTextColor">@color/dialog_button_text</item>
	<item name="buttonSeparatorColor">@color/dialog_button_separator</item>
	<item name="buttonBackgroundColorNormal">@color/dialog_button_normal</item>
	<item name="buttonBackgroundColorPressed">@color/dialog_button_pressed</item>
	<item name="buttonBackgroundColorFocused">@color/dialog_button_focused</item>
	<item name="dialogBackground">@drawable/dialog_background</item>
</style>
```

## How to create simple dialogs:

Easy:

### Dialog with a simple message and Close button:
```java
SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setMessage(R.string.message).show();
```
### Dialog with a title, message and Close button:
```java
SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle(R.string.title).setMessage(R.string.message).show();
```
### Dialog with a title, message and two buttons:	
```java
SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle(R.string.title).setMessage(R.string.message).setPositiveButtonText(R.string.positive_button).setNegativeButtonText(R.string.negative_button).show();

## Wishlist

The library could be improved with more common dialogs. Pull requests are encouraged!

### Possible candidates:
 - date picker (spinners or calendar)
 - time picker
 - edittext dialog
 - password dialog
 - ...
