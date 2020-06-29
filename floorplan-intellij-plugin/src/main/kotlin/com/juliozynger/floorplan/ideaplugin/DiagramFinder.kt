package com.juliozynger.floorplan.ideaplugin

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.FileBasedIndex

interface DatabaseSchema: PsiNameIdentifierOwner

object DiagramFinder {

    fun findDiagrams(project: Project): List<DatabaseSchema> {
        val result: MutableList<DatabaseSchema> = mutableListOf()

        val virtualFiles =
            FileBasedIndex.getInstance().getContainingFiles(
                FileTypeIndex.NAME, XmlFileType.INSTANCE, // TODO
                GlobalSearchScope.allScope(project)
            )
        for (virtualFile in virtualFiles) {
            val simpleFile: DatabaseSchema? = PsiManager.getInstance(project).findFile(virtualFile!!) as DatabaseSchema?
            if (simpleFile != null) {
                val properties = PsiTreeUtil.getChildrenOfType(simpleFile, DatabaseSchema::class.java)

                if (properties != null) {
                    result += properties
                }
            }
        }
        return result
    }
}