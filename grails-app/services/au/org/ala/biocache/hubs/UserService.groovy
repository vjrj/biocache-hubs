package au.org.ala.biocache.hubs

class UserService {
    def grailsApplication
    def authService
    def webServicesService

    /**
     * Get both email and displayName for a numeric user id.  Preferring to use the auth service
     * unless it's unavailable, then fall back to database
     *
     * @param userid The ALA userid to lookup
     */
    def detailsForUserId(String userid) {
        if (!userid) return [displayName: '', email: '']
        else if ('system' == userid) return [displayName: userid, email: userid]

        def details = null

        try {
            details = authService.getUserForUserId(userid)
        } catch (Exception e) {
            log.warn("couldn't get user details from web service", e)
        }

        if (details) return [displayName: details?.displayName ?: '', email: details?.userName ?: '']
        else {
            log.warn('could not find user details')
            return [displayName: userid, email: userid]
        }
    }

    def getUserProperty(String userid, String property) {
        def value = ''

        try {
            def resp = webServicesService.getJsonElements(grailsApplication.config.userdetails.baseUrl +
                    '/property/getProperty?alaId=' + userid +
                    '&name=' + URLEncoder.encode(grails.util.Metadata.current.'app.name' + '.' + property, "UTF-8"))

            if (resp && resp[0]?.value) {
                value = resp[0]?.value
            }
        } catch (err) {
            log.error("failed to get user property ${userid}:${property}")
        }

        value
    }

    def setUserProperty(String userid, String property, String value) {
        try {
            webServicesService.postJsonElements(grailsApplication.config.userdetails.baseUrl +
                    '/property/saveProperty?alaId=' + userid +
                    '&name=' + URLEncoder.encode(grails.util.Metadata.current.'app.name' + '.' + property, "UTF-8") +
                    '&value=' + URLEncoder.encode(value, "UTF-8"), '')
        } catch (err) {
            log.error("failed to set user property ${userid}:${property}:${value}")
        }
    }
}
