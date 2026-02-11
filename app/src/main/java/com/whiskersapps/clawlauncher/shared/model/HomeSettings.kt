package com.whiskersapps.clawlauncher.shared.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class HomeSettings: RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var buttonApp: String = ""
}