package backend.fd.seminar4.nulls.service

import backend.fd.seminar4.nulls.model.User

class UserService {

    private val users = mutableMapOf<Int, User>()

    fun addUser(user: User) {
        users[user.id] = user
    }

    fun findUserById(userId: Int): User? {
        return users[userId]
    }

    fun getUserEmail(userId: Int): String? {
        return findUserById(userId)?.email
    }

    fun getUserName(userId: Int): String? {
        return findUserById(userId)?.name
    }

    /**
     * Получение email или выброс исключения, если пользователь не найден
     */
    fun findUserEmailOrThrow(userId: Int): String {
        return getUserEmail(userId)
            ?: throw NoSuchElementException("User with ID $userId not found")
    }
}
