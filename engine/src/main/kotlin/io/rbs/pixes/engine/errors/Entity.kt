package io.rbs.pixes.engine.errors

import io.rbs.pixes.engine.Entity

open class EntityError(val entity: Entity, override val message: String) : RuntimeException(message)

class AddEntityError(entity: Entity, reason: String) : EntityError(entity, "failed to add ${entity.id}: $reason")

class RemoveEntityError(entity: Entity, reason: String) : EntityError(entity, "failed to add ${entity.id}: $reason")