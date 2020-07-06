package com.juliozynger.floorplan.ideaplugin

import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope

object DiagramFinder {

    fun findDiagrams(project: Project, databaseFqName: String): List<PsiFile> {
        val extensions = listOf("dbml", "svg", "png", "dot")

        return extensions
            .asSequence()
            .map { FileTypeManager.getInstance().getFileTypeByExtension(it) }
            .map { FileTypeIndex.getFiles(it, GlobalSearchScope.projectScope(project)) }
            .flatten()
            .filter { virtualFile: VirtualFile ->
                extensions.contains(virtualFile.extension) && virtualFile.path.contains(databaseFqName)
            }.sortedBy { it.name }
            .mapNotNull { PsiManager.getInstance(project).findFile(it) }
            .toList()
    }
}
