package com.demo.diploma.model

import com.fasterxml.jackson.annotation.JsonIgnore

class JWTModel {
    @JsonIgnore
    var sub: String? = null
    var email: String? = null
    var name: String? = null

    @JsonIgnore
    var iat: Long? = null

    @JsonIgnore
    var exp: Long? = null

    constructor()
    constructor(sub: String?, email: String?, name: String?, iat: Long?, exp: Long?) {
        this.sub = sub
        this.email = email
        this.name = name
        this.iat = iat
        this.exp = exp
    }

    override fun toString(): String {
        return "JWTModel{" +
                "sub='" + sub + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", iat=" + iat +
                ", exp=" + exp +
                '}'
    }
}