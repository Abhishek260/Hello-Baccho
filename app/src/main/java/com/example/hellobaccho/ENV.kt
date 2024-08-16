package com.example.hellobaccho

class ENV {

    companion object {

        private const val BASE_URL: String = "https://rickandmortyapi.com/api/";

        fun getBaseUrl(): String {
            return BASE_URL;
        }
    }
}