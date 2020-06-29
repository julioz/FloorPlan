package com.juliozynger.floorplan.ideaplugin

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifierListOwner
import com.juliozynger.floorplan.ideaplugin.RoomConstants.CLASS_DATABASE
import org.jetbrains.uast.UClass
import org.jetbrains.uast.toUElement

class DatabaseLineMarkerProvider: RelatedItemLineMarkerProvider() {

    private val log: Logger = Logger.getInstance(this::class.java)

    override fun getLineMarkerInfo(element: PsiElement): RelatedItemLineMarkerInfo<*>? {
        println("getLineMarkerInfo $element")
        return super.getLineMarkerInfo(element)
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>?>
    ) {
        val uElement = element.toUElement()
        // Check methods first (includes constructors).
        if (uElement is UClass) {
            log.warn("$uElement is class")
            if (element.hasAnnotation(CLASS_DATABASE)) {
                log.warn("$uElement has database annotation")
                val project: Project = element.project
                val properties: List<DatabaseSchema> = DiagramFinder.findDiagrams(project)

                if (properties.isNotEmpty()) {
                    log.warn("$uElement found $properties")
                    // Add the property to a collection of line marker info
                    val builder = NavigationGutterIconBuilder.create(FloorPlanIcons.FILE)
                        .setTargets(properties)
                        .setTooltipText("Navigate to database ER diagram")
                    result.add(builder.createLineMarkerInfo(element))
                }
            }
        }
    }

    private fun PsiElement.hasAnnotation(annotationName: String): Boolean {
        return findAnnotation(this, annotationName) != null
    }

    private fun findAnnotation(element: PsiElement, annotationName: String): PsiAnnotation? {
        if (element is PsiModifierListOwner) {
            val modifierList = element.modifierList
            if (modifierList != null) {
                for (psiAnnotation in modifierList.annotations) {
                    if (annotationName == psiAnnotation.qualifiedName) {
                        return psiAnnotation
                    }
                }
            }
        }
        return null
    }
}
