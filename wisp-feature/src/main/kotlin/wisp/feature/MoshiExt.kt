package wisp.feature

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import mu.KotlinLogging

private val logger = KotlinLogging.logger(FeatureFlags::class.qualifiedName!!)

/**
 * Attempts to use [JsonAdapter.failOnUnknown] and logs any issues before falling back to ignoring
 * the unknown fields.
 *
 * This overload is needed for JVM compatibility.
 */
fun <T> JsonAdapter<T>.toSafeJson(value: T): String = toSafeJson(value) {
  logger.warn(it) {
    "failed to parse JSON due to unknown fields. ignoring those fields and trying again"
  }
}

/**
 * Attempts to use [JsonAdapter.failOnUnknown] and logs any issues before falling back to ignoring
 * the unknown fields.
 */
fun <T> JsonAdapter<T>.toSafeJson(value: T, onUnknownFields: (JsonDataException) -> Unit): String {
  return try {
    failOnUnknown().toJson(value)
  } catch (e: JsonDataException) {
    onUnknownFields(e)
    return toJson(value)
  }
}

/**
 * Attempts to use [JsonAdapter.failOnUnknown] and logs any issues before falling back to ignoring
 * the unknown fields.
 *
 * This overload is needed for JVM compatibility.
 */
fun <T> JsonAdapter<T>.fromSafeJson(value: String): T? = fromSafeJson(value) {
  logger.warn(it) {
    "failed to parse JSON due to unknown fields. ignoring those fields and trying again"
  }
}

/**
 * Attempts to use [JsonAdapter.failOnUnknown] and logs any issues before falling back to ignoring
 * the unknown fields.
 */
fun <T> JsonAdapter<T>.fromSafeJson(json: String, onUnknownFields: (JsonDataException) -> Unit): T? {
  return try {
    failOnUnknown().fromJson(json)
  } catch (e: JsonDataException) {
    onUnknownFields(e)
    return fromJson(json)
  }
}
