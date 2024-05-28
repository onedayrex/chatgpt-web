package com.github.onedayrex.chatgptweb.toolWindow

import com.github.onedayrex.chatgptweb.ui.ChatToolWindows
import com.google.gson.JsonObject
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.ui.HoverChangesTree.Companion.getTransparentScrollbarWidth
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.IOException


class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {
        val userId:String
        val chatToolWindows = ChatToolWindows()
        val client = OkHttpClient()

        init {
            userId = "#/chat/" + System.currentTimeMillis().toString()
        }

        fun getContent() = JBPanel<JBPanel<*>>().apply {
            var keyDownListener = object : KeyListener{
                override fun keyTyped(e: KeyEvent?) {
                }

                override fun keyPressed(e: KeyEvent?) {
                    if (e?.keyCode == KeyEvent.VK_ENTER) {
                        //http
                        val param = JsonObject()
                        param.addProperty("network",true)
                        param.addProperty("prompt", chatToolWindows.questionText.text)
                        param.addProperty("stream", false)
                        param.addProperty("system","")
                        param.addProperty("userId", userId)
                        param.addProperty("withoutContext", false)

                        val requestBody =
                            RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), param.toString())

                        val request = Request.Builder()
                            .url("https://api.binjie.fun/api/generateStream?refer__1360=n4mx0DnDuGKeqD5G%3DtDsIfqCwT4fxh0T3ebrD")
                            .post(requestBody)
                            .header("Origin","https://chat18.aichatos8.xyz")
                            .build()

                        client.newCall(request).enqueue(object:Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                println(e.toString())
                            }

                            override fun onResponse(call: Call, response: Response) {
                                chatToolWindows.response.text = ""
                                val body = response.body!!
                                var steam = body.byteStream()
                                val buffer = ByteArray(1024)
                                var byteRead:Int
                                var sb = StringBuilder()
                                while (steam.read(buffer).also { byteRead = it } !=-1){
                                    sb.append(buffer.decodeToString(0,byteRead))
                                    chatToolWindows.response.text = sb.toString()
                                }
                            }

                        })
                    }
                }

                override fun keyReleased(e: KeyEvent?) {
                }

            }
            chatToolWindows.questionText.addKeyListener(keyDownListener)
            add(chatToolWindows.panel1)
        }
    }
}
