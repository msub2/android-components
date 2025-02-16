/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.browser.awesomebar.layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import mozilla.components.browser.awesomebar.BrowserAwesomeBar
import mozilla.components.browser.awesomebar.R
import mozilla.components.browser.awesomebar.widget.FlowLayout
import mozilla.components.concept.awesomebar.AwesomeBar
import mozilla.components.support.ktx.android.util.dpToPx
import mozilla.components.support.test.mock
import mozilla.components.support.test.robolectric.testContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultSuggestionViewHolderTest {

    @After
    fun tearDown() {
        MAX_TEXT_LENGTH = 250
    }

    @Test
    fun `DefaultViewHolder sets title and description`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_generic, null, false
        )

        val awesomeBar = BrowserAwesomeBar(testContext)
        val viewHolder = DefaultSuggestionViewHolder.Default(awesomeBar, view)

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            title = "Hello World",
            description = "https://www.mozilla.org"
        )

        viewHolder.bind(suggestion) {
            // Do nothing
        }

        val titleView = view.findViewById<TextView>(R.id.mozac_browser_awesomebar_title)
        assertEquals("Hello World", titleView.text)

        val descriptionView = view.findViewById<TextView>(R.id.mozac_browser_awesomebar_description)
        assertEquals("https://www.mozilla.org", descriptionView.text)
        assertEquals(View.VISIBLE, descriptionView.visibility)
    }

    @Test
    fun `DefaultViewHolder has a rotated image for edit button if set as such in awesomebar`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_generic, null, false
        )

        val awesomeBar = BrowserAwesomeBar(testContext)
        awesomeBar.customizeForBottomToolbar = true
        val viewHolder = DefaultSuggestionViewHolder.Default(awesomeBar, view)

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            title = "Hello World",
            description = "https://www.mozilla.org",
            editSuggestion = "https://www.mozilla.org"
        )

        viewHolder.bind(suggestion, awesomeBar.customizeForBottomToolbar) {
            // Do nothing
        }

        val editArrowButton = view.findViewById<ImageButton>(R.id.mozac_browser_awesomebar_edit_suggestion)
        assertEquals(View.VISIBLE, editArrowButton.visibility)
        assertEquals(270f, editArrowButton.rotation)
    }

    @Test
    fun `DefaultViewHolder does not have a rotated image for edit button if set as such in awesomebar`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_generic, null, false
        )

        val awesomeBar = BrowserAwesomeBar(testContext)
        awesomeBar.customizeForBottomToolbar = false
        val viewHolder = DefaultSuggestionViewHolder.Default(awesomeBar, view)

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            title = "Hello World",
            description = "https://www.mozilla.org",
            editSuggestion = "https://www.mozilla.org"
        )

        viewHolder.bind(suggestion, awesomeBar.customizeForBottomToolbar) {
            // Do nothing
        }

        val editArrowButton = view.findViewById<ImageButton>(R.id.mozac_browser_awesomebar_edit_suggestion)
        assertEquals(View.VISIBLE, editArrowButton.visibility)
        assertEquals(0f, editArrowButton.rotation)
    }

    @Test
    fun `DefaultViewHolder without description hides description view`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_generic, null, false
        )

        val awesomeBar = BrowserAwesomeBar(testContext)
        val viewHolder = DefaultSuggestionViewHolder.Default(awesomeBar, view)

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            title = "Hello World"
        )

        viewHolder.bind(suggestion) {
            // Do nothing
        }

        val titleView = view.findViewById<TextView>(R.id.mozac_browser_awesomebar_title)
        assertEquals("Hello World", titleView.text)

        val descriptionView = view.findViewById<TextView>(R.id.mozac_browser_awesomebar_description)
        assertEquals(View.GONE, descriptionView.visibility)
    }

    @Test
    fun `Clicking on default suggestion view invokes callback`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_generic, null, false
        )

        val viewHolder = DefaultSuggestionViewHolder.Default(
            BrowserAwesomeBar(testContext), view
        )

        var callbackExecuted = false
        val suggestion = AwesomeBar.Suggestion(
            mock(),
            onSuggestionClicked = { callbackExecuted = true }
        )

        view.performClick()
        assertFalse(callbackExecuted)

        viewHolder.bind(suggestion) {
            // Do nothing
        }

        view.performClick()
        assertTrue(callbackExecuted)
    }

    @Test
    fun `Clicking on edit suggestion button invokes callback`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_generic, null, false
        )

        var callbackExecuted = false

        val viewHolder = DefaultSuggestionViewHolder.Default(
            BrowserAwesomeBar(testContext).apply {
                setOnEditSuggestionListener {
                    assertEquals("Hello World", it)
                    callbackExecuted = true
                }
            },
            view
        )

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            editSuggestion = "Hello World"
        )

        viewHolder.bind(suggestion) {
            // Do nothing
        }

        view.findViewById<View>(R.id.mozac_browser_awesomebar_edit_suggestion).performClick()
        assertTrue(callbackExecuted)
    }

    @Test
    fun `Edit suggestion button is hidden when editSuggestion is empty`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_generic, null, false
        )

        val viewHolder = DefaultSuggestionViewHolder.Default(
            BrowserAwesomeBar(testContext),
            view
        )

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            editSuggestion = ""
        )

        viewHolder.bind(suggestion) {
            // Do nothing
        }

        val editSuggestionView = view.findViewById<View>(R.id.mozac_browser_awesomebar_edit_suggestion)
        assertEquals(View.GONE, editSuggestionView.visibility)
    }

    @Test
    fun `ChipsSuggestionViewHolder adds views for chips`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_chips, null, false
        )

        val viewHolder = DefaultSuggestionViewHolder.Chips(
            BrowserAwesomeBar(testContext), view
        )

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            chips = listOf(
                AwesomeBar.Suggestion.Chip("Hello"),
                AwesomeBar.Suggestion.Chip("World"),
                AwesomeBar.Suggestion.Chip("Example")
            )
        )

        val container = view.findViewById<ViewGroup>(R.id.mozac_browser_awesomebar_chips)

        assertEquals(0, container.childCount)

        viewHolder.bind(suggestion) {
            // Do nothing.
        }

        assertEquals(3, container.childCount)

        assertEquals("Hello", (container.getChildAt(0) as TextView).text)
        assertEquals("World", (container.getChildAt(1) as TextView).text)
        assertEquals("Example", (container.getChildAt(2) as TextView).text)
    }

    @Test
    fun `Clicking on a chip invokes callback`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_chips, null, false
        )

        val viewHolder = DefaultSuggestionViewHolder.Chips(
            BrowserAwesomeBar(testContext), view
        )

        var chipClicked: String? = null

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            chips = listOf(
                AwesomeBar.Suggestion.Chip("Hello"),
                AwesomeBar.Suggestion.Chip("World"),
                AwesomeBar.Suggestion.Chip("Example")
            ),
            onChipClicked = {
                chipClicked = it.title
            }
        )

        viewHolder.bind(suggestion) {
            // Do nothing.
        }

        val container = view.findViewById<ViewGroup>(R.id.mozac_browser_awesomebar_chips)

        container.getChildAt(0).performClick()
        assertEquals("Hello", chipClicked)

        container.getChildAt(1).performClick()
        assertEquals("World", chipClicked)

        container.getChildAt(2).performClick()
        assertEquals("Example", chipClicked)
    }

    @Test
    fun `FlowLayout for chips has spacing applied`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_chips, null, false
        )

        val flowLayout = view.findViewById<FlowLayout>(R.id.mozac_browser_awesomebar_chips)

        assertEquals(0, flowLayout.spacing)

        val awesomeBar = BrowserAwesomeBar(testContext)
        DefaultSuggestionViewHolder.Chips(awesomeBar, view)

        assertEquals(2.dpToPx(testContext.resources.displayMetrics), flowLayout.spacing)
    }

    @Test
    fun `DefaultViewHolder truncates title and description if needed`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_generic, null, false
        )

        MAX_TEXT_LENGTH = 5
        val awesomeBar = BrowserAwesomeBar(testContext)
        val viewHolder = DefaultSuggestionViewHolder.Default(awesomeBar, view)

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            title = "123456789",
            description = "123456789"
        )

        viewHolder.bind(suggestion) {
            // Do nothing
        }

        val titleView = view.findViewById<TextView>(R.id.mozac_browser_awesomebar_title)
        assertEquals("12345", titleView.text)

        val descriptionView = view.findViewById<TextView>(R.id.mozac_browser_awesomebar_description)
        assertEquals("12345", descriptionView.text)
        assertEquals(View.VISIBLE, descriptionView.visibility)
    }

    @Test
    fun `ChipsSuggestionViewHolder truncates title if needed`() {
        val view = LayoutInflater.from(testContext).inflate(
            R.layout.mozac_browser_awesomebar_item_chips, null, false
        )

        MAX_TEXT_LENGTH = 5
        val viewHolder = DefaultSuggestionViewHolder.Chips(
            BrowserAwesomeBar(testContext), view
        )

        val suggestion = AwesomeBar.Suggestion(
            mock(),
            chips = listOf(AwesomeBar.Suggestion.Chip("123456789"))
        )

        val container = view.findViewById<ViewGroup>(R.id.mozac_browser_awesomebar_chips)

        assertEquals(0, container.childCount)

        viewHolder.bind(suggestion) {
            // Do nothing.
        }

        assertEquals(1, container.childCount)
        assertEquals("12345", (container.getChildAt(0) as TextView).text)
    }
}
