const ACCESS_TOKEN = 'access_token'
const REFRESH_TOKEN = 'refresh_token'
const EXPIRES = 'token_expires'

const isExpired = () => {
    const expires = window.localStorage.getItem(EXPIRES) ? parseInt(window.localStorage.getItem(EXPIRES)) : null
    if (!expires) {
        AuthUtils.removeAuth()
        return true
    }
    if (Date.now() > expires) {
        AuthUtils.removeAuth()
        return true
    }
    return false
}

const AuthUtils = {
    getToken : () => {
        if (isExpired()) {
            return null
        }
        return window.localStorage.getItem(ACCESS_TOKEN)
    },

    getRefreshToken : () => {
        if (isExpired()) {
            return null
        }
        return window.localStorage.getItem(REFRESH_TOKEN)
    },

    removeAuth : () => {
        window.localStorage.removeItem(ACCESS_TOKEN)
        window.localStorage.removeItem(REFRESH_TOKEN)
        window.localStorage.removeItem(EXPIRES)
    },

    setToken : (data) => {
        if (!data && !data.accessToken) {
            return
        }
        window.localStorage.setItem(ACCESS_TOKEN, data.accessToken)
        window.localStorage.setItem(REFRESH_TOKEN, data.refreshToken)
        window.localStorage.setItem(EXPIRES, new Date(data.refreshTokenExpires).getTime())
    }
}

export default AuthUtils