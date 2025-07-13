package com.nanit.birthday.data.mapper

import com.nanit.birthday.data.local.entity.BabyEntity
import com.nanit.birthday.domain.model.Baby

/**
 * Converts a data layer [BabyEntity] to domain layer [Baby].
 *
 * @return Domain [Baby] model
 */
fun BabyEntity.toDomain() =
    Baby(
        id = id,
        name = name,
        birthday = birthday,
        pictureUri = pictureUri
    )

/**
 * Converts a domain layer [Baby] to data layer [BabyEntity].
 *
 * @return Data layer [BabyEntity]
 */
fun Baby.toEntity() =
    BabyEntity(
        id = id,
        name = name,
        birthday = birthday,
        pictureUri = pictureUri
    )

/**
 * Converts a list of [BabyEntity] to list of [Baby] domain models.
 *
 * @return List of domain [Baby] models
 */
fun List<BabyEntity>.toDomain() = map { it.toDomain() }

/**
 * Converts a list of domain [Baby] models to list of [BabyEntity].
 *
 * @return List of data [BabyEntity]
 */
fun List<Baby>.toEntity() = map { it.toEntity() }