package com.juliozynger.floorplan.ideaplugin

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FileBasedIndex
import java.util.*


interface DatabaseSchema: PsiNameIdentifierOwner

object DiagramFinder {

    fun findDiagrams(project: Project, databaseFqName: String): List<DatabaseSchema> {
        val result: MutableList<DatabaseSchema> = mutableListOf()

        val extensions = listOf("dbml", "svg", "png", "dot")

        val fileTypes: Set<FileType> = setOf(
            FileTypeManager.getInstance().getFileTypeByFileName("dbml"),
            FileTypeManager.getInstance().getFileTypeByFileName("png"),
            FileTypeManager.getInstance().getFileTypeByFileName("svg"),
            FileTypeManager.getInstance().getFileTypeByFileName("dot")
        )
        val fileList: MutableList<VirtualFile> = ArrayList()
        FileBasedIndex.getInstance().processFilesContainingAllKeys(
            FileTypeIndex.NAME,
            fileTypes,
            GlobalSearchScope.projectScope(project),
            null,
            Processor { virtualFile: VirtualFile ->
                if (extensions.contains(virtualFile.extension) && virtualFile.path.contains(databaseFqName)) {
                    fileList.add(virtualFile)
                }
                true
            }
        )
        println(fileList)
//        LocalFileSystem.getInstance().findFileByIoFile()
//
//        val virtualFiles =
//            FileBasedIndex.getInstance().getContainingFiles(
//                FileTypeIndex.NAME, XmlFileType.INSTANCE, // TODO
//                GlobalSearchScope.allScope(project)
//            )
//        for (virtualFile in virtualFiles) {
//            val simpleFile: DatabaseSchema? = PsiManager.getInstance(project).findFile(virtualFile!!) as DatabaseSchema?
//            if (simpleFile != null) {
//                val properties = PsiTreeUtil.getChildrenOfType(simpleFile, DatabaseSchema::class.java)
//
//                if (properties != null) {
//                    result += properties
//                }
//            }
//        }
        return result
    }
}