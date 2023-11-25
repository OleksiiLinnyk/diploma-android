package com.demo.diploma.deserializer

import com.demo.diploma.model.ExerciseType
import com.demo.diploma.model.ExerciseType.SINGLE_ANSWER_EXERCISE
import com.demo.diploma.model.IExercise
import com.demo.diploma.model.OpenExercise
import com.demo.diploma.model.TestExercise
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import java.lang.reflect.Type


class IExerciseDeserializer : JsonDeserializer<IExercise> {

    private val objectMapper: ObjectMapper = ObjectMapper()
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): IExercise {
        val root = json?.asJsonObject
        val type: ExerciseType? = root?.get("type")?.asString?.uppercase()
            ?.let { ExerciseType.valueOf(it) }
        if (type == SINGLE_ANSWER_EXERCISE || type == ExerciseType.MULTIPLE_ANSWER_EXERCISE) {
            return TestExercise(
                type,
                root.get("question")?.toString()?.replace("\"", ""),
                objectMapper.readValue(root.get("options").toString(),
                    object : TypeReference<List<String>>() {}),
                objectMapper.readValue(
                    root.get("givenAnswerIndexes").toString(),
                    object : TypeReference<List<Int>>() {}),
                root.get("points").asInt
            )
        }
        val question = if(root?.get("question") !is JsonNull)
            root?.get("question")?.toString()?.replace("\"", "")
        else null
        val givenAnswer = if (root?.get("givenAnswer") !is JsonNull)
            root?.get("givenAnswer")?.asString?.replace("\"", "")
        else null
        return OpenExercise(type, question, givenAnswer, root?.get("points")?.asInt ?: 0)
    }
}