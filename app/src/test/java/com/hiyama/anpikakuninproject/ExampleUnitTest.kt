package com.hiyama.anpikakuninproject

import com.hiyama.anpikakuninproject.activity.NotificationsFragment
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test(){
        val notification = Mockito.mock(NotificationsFragment::class.java)
        Mockito.`when`(notification.getInfo()).thenReturn("{{title: '1', content: 'test'},\n" +
                "{title: '2', content: 'test2'}}")
//        println(notification.getNotification())
    }
}