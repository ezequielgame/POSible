package com.progdist.egm.proyectopdist.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.progdist.egm.proyectopdist.adapter.CreditsListAdapter
import com.progdist.egm.proyectopdist.data.Credit
import com.progdist.egm.proyectopdist.databinding.ActivityCreditsBinding


class CreditsActivity : AppCompatActivity() {

    lateinit var _binding: ActivityCreditsBinding
    val binding get() = _binding

    lateinit var recyclerAdapter: CreditsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        var credits: ArrayList<Credit> = ArrayList<Credit>()
        credits.add(Credit(name = "Retrofit 2 - \nA type-safe HTTP client for Android and Java.", details = "Copyright 2013 Square, Inc.\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.", url="https://github.com/square/retrofit"))

        credits.add(Credit(name="OkHttp - \nHTTP is the way modern applications network. It’s how we exchange data & media. Doing HTTP efficiently makes your stuff load faster and saves bandwidth.",
            details="Copyright 2019 Square, Inc.\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.",
            url="https://github.com/square/okhttp"))

        credits.add(Credit(name="code scanner - \nCode scanner library for Android, based on ZXing",
            details="MIT License\n" +
                    "\n" +
                    "Copyright (c) 2017 Yuriy Budiyev [yuriy.budiyev@yandex.ru]\n" +
                    "\n" +
                    "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                    "of this software and associated documentation files (the \"Software\"), to deal\n" +
                    "in the Software without restriction, including without limitation the rights\n" +
                    "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                    "copies of the Software, and to permit persons to whom the Software is\n" +
                    "furnished to do so, subject to the following conditions:\n" +
                    "\n" +
                    "The above copyright notice and this permission notice shall be included in all\n" +
                    "copies or substantial portions of the Software.\n" +
                    "\n" +
                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                    "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                    "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                    "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                    "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                    "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                    "SOFTWARE.",
            url="https://github.com/yuriy-budiyev/code-scanner"))

        credits.add(Credit(name="CircleImageView - \nA fast circular ImageView perfect for profile images.",
            details="Copyright 2014 - 2020 Henning Dodenhof\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.",
            url="https://github.com/hdodenhof/CircleImageView"))

        credits.add(Credit(name="Undraw - \nOpen-source illustrations for any idea you can imagine and create.",
            details="Copyright 2022 Katerina Limpitsouni\n" +
                    "All images, assets and vectors published on unDraw can be used for free. You can use them for noncommercial and commercial purposes. You do not need to ask permission from or provide credit to the creator or unDraw.\n" +
                    "\n" +
                    "More precisely, unDraw grants you an nonexclusive, worldwide copyright license to download, copy, modify, distribute, perform, and use the assets provided from unDraw for free, including for commercial purposes, without permission from or attributing the creator or unDraw. This license does not include the right to compile assets, vectors or images from unDraw to replicate a similar or competing service, in any form or distribute the assets in packs or otherwise. This extends to automated and non-automated ways to link, embed, scrape, search or download the assets included on the website without our consent.\n" +
                    "\n" +
                    "Regarding brand logos that are included:\n" +
                    "Are registered trademarks of their respected owners. Are included on a promotional basis and do not represent an association with unDraw or its users. Do not indicate any kind of endorsement of the trademark holder towards unDraw, nor vice versa. Are provided with the sole purpose to represent the actual brand/service/company that has registered the trademark and must not be used otherwise.",
            url="https://undraw.co/"))

        credits.add(Credit(name="icons8",
            details="Icons, illustrations, photos, music, and design tools",
            url="https://icons8.com/"))

        recyclerAdapter.setOnItemClickListener(object : CreditsListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val url = recyclerAdapter.getCredit(position).url
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        })

        recyclerAdapter.setOnItemLongClickListener(object :
            CreditsListAdapter.onItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                val url = recyclerAdapter.getCredit(position).url
                binding.root.showToast(url)
                return true
            }
        })

        recyclerAdapter.setCreditsList(credits)
    }

    private fun initRecyclerView() {

        binding.creditsRecycler.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = CreditsListAdapter()
        binding.creditsRecycler.adapter = recyclerAdapter

    }

}