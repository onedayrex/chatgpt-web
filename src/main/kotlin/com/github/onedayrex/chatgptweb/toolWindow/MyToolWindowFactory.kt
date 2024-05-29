package com.github.onedayrex.chatgptweb.toolWindow

import com.github.onedayrex.chatgptweb.ui.ChatToolWindows
import com.google.gson.JsonObject
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.jetbrains.io.response
import java.awt.BorderLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.IOException
import javax.swing.JPanel
import javax.swing.JTextField


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
                        chatToolWindows.questionText.text = ""
//                        chatToolWindows.questionText.isEditable = false
                        var responsePanel = JPanel()
                        responsePanel.layout = BorderLayout()
                        val responseText = JTextField("loading...")
                        responsePanel.add(responseText,BorderLayout.CENTER)
                        chatToolWindows.jScrollPane.setViewportView(responsePanel)
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

//                        client.newCall(request).enqueue(object:Callback {
//                            override fun onFailure(call: Call, e: IOException) {
//                                chatToolWindows.questionText.isEditable = true
//                                println(e.toString())
//                            }
//
//                            override fun onResponse(call: Call, response: Response) {
//                                val body = response.body!!
//                                var steam = body.byteStream()
//                                val buffer = ByteArray(1024)
//                                var byteRead:Int
//                                var sb = StringBuilder()
//                                while (steam.read(buffer).also { byteRead = it } !=-1){
//                                    val decodeToString = buffer.decodeToString(0, byteRead)
//                                    sb.append(decodeToString)
//                                    print(decodeToString)
////                                    chatToolWindows.response.text = sb.toString()
//                                }
//                                chatToolWindows.questionText.isEditable = true
//                            }
//
//                        })
                    }
                }

                override fun keyReleased(e: KeyEvent?) {
                }

            }
            layout = BorderLayout()
            chatToolWindows.questionText.addKeyListener(keyDownListener)
            add(chatToolWindows.panel)
        }
    }
}
