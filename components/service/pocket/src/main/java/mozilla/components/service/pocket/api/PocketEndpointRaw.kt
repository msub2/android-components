/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.api

import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import mozilla.components.concept.fetch.Client
import mozilla.components.concept.fetch.Request
import mozilla.components.service.pocket.api.PocketEndpointRaw.Companion.newInstance
import mozilla.components.service.pocket.api.ext.fetchBodyOrNull

/**
 * Makes requests to the Pocket endpoint and returns the raw JSON data.
 *
 * @see [PocketEndpoint], which wraps this to make it more practical.
 * @see [newInstance] to retrieve an instance.
 */
internal class PocketEndpointRaw internal constructor(
    @VisibleForTesting internal val client: Client
) {
    /**
     * Gets the current stories recommendations from the Pocket server.
     *
     * @return The stories recommendations as a raw JSON string or null on error.
     */
    @WorkerThread
    fun getRecommendedStories(): String? = makeRequest()

    /**
     * @return The requested JSON as a String or null on error.
     */
    @WorkerThread // synchronous request.
    private fun makeRequest(): String? {
        val request = Request(pocketEndpointUrl)
        return client.fetchBodyOrNull(request)
    }

    companion object {
        private const val pocketEndpointUrl = "https://firefox-android-home-recommendations.getpocket.dev/"

        /**
         * Returns a new instance of [PocketEndpointRaw].
         *
         * @param client the HTTP client to use for network requests.
         */
        fun newInstance(client: Client): PocketEndpointRaw {
            return PocketEndpointRaw(client)
        }
    }
}
