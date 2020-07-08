package com.juliozynger.floorplan.ideaplugin

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.juliozynger.floorplan.ideaplugin.RoomConstants.CLASS_DATABASE
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.toUElement

class DatabaseLineMarkerProvider: RelatedItemLineMarkerProvider() {

    private val log: Logger = Logger.getInstance(this::class.java)

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>?>
    ) {
        val uElement = element.toUElement()
        val parent = uElement?.uastParent
        val grandparent = parent?.uastParent
        if (uElement is UIdentifier && parent is UAnnotation && grandparent is UClass) {
            if (parent.qualifiedName == CLASS_DATABASE) {
                val databaseQualifiedName: String = grandparent.qualifiedName!!
                log.debug("$databaseQualifiedName has database annotation")
                val project: Project = element.project
                val schemaFiles: List<PsiFile> = DiagramFinder.findDiagrams(project, databaseQualifiedName)

                if (schemaFiles.isNotEmpty()) {
                    log.debug("${grandparent.qualifiedName} found $schemaFiles")
                    // Add the property to a collection of line marker info
                    val builder = NavigationGutterIconBuilder.create(FloorPlanIcons.FILE)
                        .setTargets(schemaFiles)
                        .setTooltipText("Navigate to database ER diagram")
                    result.add(builder.createLineMarkerInfo(element))
                }
            }
        }
    }
}
