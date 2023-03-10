package lk.nibm.holidayfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    lateinit var spinnerCountry: Spinner
    lateinit var spinnerYear : Spinner
    lateinit var nextbtn : Button

    var selectedCountryCode: String? = null
    var selectedYear: String? = null

    val countries = listOf(
        Pair("lk", "Sri Lanka"),
        Pair("us", "United States"),
        Pair("uk", "United Kingdom"),
        Pair("in", "India"),
        Pair("jp", "Japan"),

        )

    val years = arrayOf("2020", "2021", "2022", "2023", "2024","2025")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerCountry = findViewById(R.id.spinner_country)
        spinnerYear = findViewById(R.id.spinner_year)
        nextbtn = findViewById(R.id.btn_next)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            countries.map { it.second })
        spinnerCountry.adapter = adapter

        val adapteryears = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,years)
        spinnerYear.adapter = adapteryears


        spinnerCountry.onItemSelectedListener = this
        spinnerYear.onItemSelectedListener = this


    }


       override  fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {


           when (parent.id) {
               R.id.spinner_country -> {
                   val selectedPair = countries[position]
                   selectedCountryCode = selectedPair.first
                  // Log.e("Spinner", "Selected country code: $selectedCountryCode")
               }
               R.id.spinner_year -> {
                   selectedYear = parent.getItemAtPosition(position) as String
                  // Log.e("Spinner", "Selected year: $selectedYear")
               }
           }
           if (selectedCountryCode != null && selectedYear != null) {

               nextbtn.setOnClickListener {
                   getHolidaydata(selectedCountryCode!!,selectedYear!! )
               }

           }


       }



    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


     fun getHolidaydata( selectedCountryCode: String,selectedYear: String) {

         Log.e("output", "Selected code: $selectedCountryCode")
         Log.e("output", "Selected year: $selectedYear")

         val url = "https://calendarific.com/api/v2/holidays?&api_key=afd135c1d18af776c23617bb89d0b0f63651bc89&country="+selectedCountryCode+"&year="+selectedYear+""
       Log.e("output", "Selected year: $url")

         val request = StringRequest(Request.Method.GET,url,Response.Listener { response ->

             val obj = JSONObject(response)
             val holidaysArray = obj.getJSONArray("holidays")

             for (i in 0 until holidaysArray.length()) {
                 val holiday = holidaysArray.getJSONObject(i)
                 val name = holiday.getString("name")
                 val description = holiday.getString("description")

                 Log.d("Holiday", "Name: $name, Description: $description")
                 print(name)
             }

         },
             Response.ErrorListener { error ->

             })
    }


}




