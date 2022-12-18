package com.amrg.herafi.data.local.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.amrg.herafi.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<User> {

    override val defaultValue = User()

    override suspend fun readFrom(input: InputStream): User {
        try {
            return Json.decodeFromString(
                User.serializer(), input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(User.serializer(), t)
                    .encodeToByteArray()
            )
        }
    }
}