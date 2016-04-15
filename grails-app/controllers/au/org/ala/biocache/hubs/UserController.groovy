/*
 * Copyright (C) 2014 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.biocache.hubs

import org.apache.commons.httpclient.HttpStatus

/**
 * User properties
 */
class UserController {
    def grailsApplication
    def authService
    def userService

    /**
     * set user property
     *
     * @return
     */
    def setProperty() {
        if (!grailsApplication.config?.userdetails?.baseUrl) {
            render status: HttpStatus.SC_FORBIDDEN
        } else {
            def user = authService.getUserId()
            if (!user) {
                render status: HttpStatus.SC_FORBIDDEN
            } else if (!params?.name || params?.value == null) {
                render status: HttpStatus.SC_BAD_REQUEST
            } else {
                userService.setUserProperty(user, params.name, params.value)
                render status: HttpStatus.SC_OK
            }
        }
    }

    /**
     * get user property
     *
     * @return
     */
    def getProperty() {
        if (!grailsApplication.config?.userdetails?.baseUrl) {
            render status: HttpStatus.SC_FORBIDDEN
        } else {
            def user = authService.getUserId()
            if (!user) {
                render status: HttpStatus.SC_FORBIDDEN
            } else if (!params?.name) {
                render status: HttpStatus.SC_BAD_REQUEST
            } else {
                render status: HttpStatus.SC_OK, text: userService.getUserProperty(user, params.name)
            }
        }
    }

}
