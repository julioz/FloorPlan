package com.juliozynger.floorplan.ideaplugin

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import com.intellij.util.ReflectionUtil
import org.jetbrains.kotlin.asJava.namedUnwrappedElement
import org.junit.Ignore
import java.lang.IllegalStateException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.concurrent.DelayQueue
import javax.swing.Timer

class DatabaseLineMarkerProviderTest: LightJavaCodeInsightFixtureTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    fun testNoDatabaseAnnotationPresent() {
        myFixture.configureByFiles(
            getTestName(false) + ".kt",
            "Database.kt"
        )
        val editor: Editor = myFixture.editor
        val project: Project = myFixture.project

        myFixture.doHighlighting()

        val infoList = DaemonCodeAnalyzerImpl.getLineMarkers(editor.document, project)
        assertEquals(0, infoList.size)
    }

    fun testDatabaseAnnotationPresentButNoDiagrams() {
        myFixture.configureByFiles(
            getTestName(false) + ".kt",
            "Database.kt"
        )
        val editor: Editor = myFixture.editor
        val project: Project = myFixture.project

        myFixture.doHighlighting()

        val infoList = DaemonCodeAnalyzerImpl.getLineMarkers(editor.document, project)
        assertEquals(0, infoList.size)
    }

    fun testDatabaseAnnotationPresentWithOneDiagram() {
        myFixture.configureByFiles(
            getTestName(false) + ".kt",
            "Database.kt",
            "com.sampledata.floorplan.MyDatabase/diagram.svg"
        )

        val editor: Editor = myFixture.editor
        val project: Project = myFixture.project

        myFixture.doHighlighting()

        DaemonCodeAnalyzerImpl.getLineMarkers(editor.document, project)
            .assertNumberOfGutterMarkers(1)
            .assertTooltipOnGutterMarkers("Navigate to database ER diagram")
            .assertNavigationTargets(listOf("diagram.svg"))
    }

    fun testDatabaseAnnotationPresentWithSecondAnnotationInFile() {
        myFixture.configureByFiles(
            getTestName(false) + ".kt",
            "Database.kt",
            "com.sampledata.floorplan.MyDatabase/diagram.svg"
        )

        val editor: Editor = myFixture.editor
        val project: Project = myFixture.project

        myFixture.doHighlighting()

        DaemonCodeAnalyzerImpl.getLineMarkers(editor.document, project)
            .assertNumberOfGutterMarkers(1)
            .assertTooltipOnGutterMarkers("Navigate to database ER diagram")
            .assertNavigationTargets(listOf("diagram.svg"))
    }

    private fun List<LineMarkerInfo<*>>.assertNumberOfGutterMarkers(expectedNumberOfMarkers: Int): List<LineMarkerInfo<*>> {
        assertEquals(expectedNumberOfMarkers, size)
        return this
    }

    private fun List<LineMarkerInfo<*>>.assertTooltipOnGutterMarkers(expectedTooltip: String): List<LineMarkerInfo<*>> {
        forEach {
            assertEquals(expectedTooltip, it.lineMarkerTooltip)
        }
        return this
    }

    private fun List<LineMarkerInfo<*>>.assertNavigationTargets(targetFileNames: List<String>): List<LineMarkerInfo<*>> {
        assertEquals(targetFileNames.size, size)
        forEachIndexed { index, lineMarker ->
            assertTrue(lineMarker is RelatedItemLineMarkerInfo)
            val relatedItem = (lineMarker as RelatedItemLineMarkerInfo).createGotoRelatedItems().toList()[index]

            assertTrue(relatedItem.element is PsiFile)
            val navTargetName = (relatedItem.element as PsiFile).name
            assertEquals(targetFileNames[index], navTargetName)
        }
        return this
    }

    override fun tearDown() {
        checkJavaSwingTimersAreDisposed()
        super.tearDown()
    }
    private fun checkJavaSwingTimersAreDisposed() {
        // NOTE: added this otherwise plugin tests fail due to swing timers not being disposed which have nothing to do with the plugin test
        // see https://intellij-support.jetbrains.com/hc/en-us/community/posts/360006918780-Tests-Fail-due-to-Java-Swing-Timers-Not-Disposed
        try {
            val timerQueueClass = Class.forName("javax.swing.TimerQueue")
            val sharedInstance: Method = timerQueueClass.getMethod("sharedInstance")
            sharedInstance.isAccessible = true
            val timerQueue: Any = sharedInstance.invoke(null)
            println("Obtained TimerQueue for timer disposal.")
            val delayQueue: DelayQueue<*> =
                ReflectionUtil.getField(timerQueueClass, timerQueue, DelayQueue::class.java, "queue")
            while (delayQueue.peek() != null) {
                println("Peeking on TimerQueue...")
                val timer = delayQueue.peek() ?: return
                val getTimer: Method? = ReflectionUtil.getDeclaredMethod(timer.javaClass, "getTimer")
                val swingTimer: Timer = getTimer?.invoke(timer) as Timer
                println("Got a timer $swingTimer")
                swingTimer.stop()
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

}
