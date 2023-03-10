package com.example.jetpack

import android.text.Html

class Test {

}

class Td{
    var content:String = ""

    fun html():String{
        return "\n\t\t<td>$content</td>"
    }
}

class Tr{
    private val children = ArrayList<Td>()
    fun td(block:Td.()->String){
        val td = Td()
        td.content = td.block()
        children.add(td)
    }

    fun html():String{
        val builder = java.lang.StringBuilder()
        builder.append("\n\t<tr>")
        for (childTag in children){
            builder.append(childTag.html())
        }
        builder.append("\n\t</tr>")
        return builder.toString()
    }
}

class Table{
    private val children= ArrayList<Tr>()
    fun tr(block:Tr.()->Unit){
        val tr = Tr()
        tr.block()
        children.add(tr)
    }

    fun html():String{
        val builder = java.lang.StringBuilder()
        builder.append("<table>")
        for (childTag in children){
            builder.append(childTag.html())
        }
        builder.append("\n</tr>")
        return builder.toString()
    }
}

fun table(block:Table.()->Unit):String{
    val table = Table()
    table.block()
    return table.html()
}



fun main() {
    val string =  table {
        tr {
            td { "Apple1" }
            td { "Grape1" }
            td { "Orange1" }
        }
        tr {
            td { "Apple2" }
            td { "Grape2" }
            td { "Orange2" }
        }
    }
    println(string)

//    dependencies {
//        implementation("1111")
//        implementation("22222")
//    }
}

class Dependency{
    val libraries = ArrayList<String>()

    fun implementation(lib:String){
        libraries.add(lib)
    }
}

fun dependencies(block:Dependency.()->Unit):List<String>{
    val dependency = Dependency()
    dependency.block()
    return dependency.libraries
}