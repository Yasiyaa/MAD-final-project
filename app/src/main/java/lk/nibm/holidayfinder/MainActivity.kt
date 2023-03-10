package lk.nibm.holidayfinder

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject



class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    lateinit var spinnerCountry: Spinner
    lateinit var spinnerYear : Spinner
    lateinit var nextbtn : Button
    lateinit var recyclerView: RecyclerView
    lateinit var progressbar : ProgressBar

    var selectedCountryCode: String? = null
    var selectedYear: String? = null
    val handler = android.os.Handler()


    val countries = listOf(
        Pair("lk", "Sri Lanka"),
        Pair("us", "United States"),
        Pair("uk", "United Kingdom"),
        Pair("in", "India"),
        Pair("jp", "Japan"),

        )

    val years = arrayOf("2020", "2021", "2022", "2023", "2024","2025","2026","2027")

    var holidaydetails = JSONArray()






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null) {

            supportActionBar!!.hide()

        }

        spinnerCountry = findViewById(R.id.spinner_country)
        spinnerYear = findViewById(R.id.spinner_year)
        nextbtn = findViewById(R.id.btn_next)
        recyclerView = findViewById(R.id.recycleview)
        progressbar = findViewById(R.id.progress_Bar)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            countries.map { it.second })
        spinnerCountry.adapter = adapter

        val adapteryears = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,years)
        spinnerYear.adapter = adapteryears


        spinnerCountry.onItemSelectedListener = this
        spinnerYear.onItemSelectedListener = this

        recyclerView.layoutManager  = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL,false)

        recyclerView.adapter = HolidayApadtor()

        // Set the default value of the spinner to the current system year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        spinnerYear.setSelection(adapteryears.getPosition(currentYear))


       // getLocation()


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

                   var i = 0
                   progressbar.visibility = VISIBLE
                   i = progressbar.progress

                   Thread(Runnable {

                       while (i < 100) {
                           i += 10
                           handler.post(Runnable {
                               progressbar.progress = i
                           })
                           try {
                               Thread.sleep(100)
                           } catch (e: InterruptedException) {
                               e.printStackTrace()
                           }
                       }



                       progressbar.visibility = INVISIBLE
                   }).start()

                   getHolidaydata(selectedCountryCode!!,selectedYear!! )


               }

           }


       }



    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }



    fun getHolidaydata(selectedCountryCode: String, selectedYear: String) {

        Log.e("output", "Selected code: $selectedCountryCode")
        Log.e("output", "Selected year: $selectedYear")

         val url = "https://calendarific.com/api/v2/holidays?&api_key=afd135c1d18af776c23617bb89d0b0f63651bc89&country=" + selectedCountryCode + "&year=" + selectedYear + ""
      Log.e("url",url)


        val result = StringRequest(Request.Method.GET,url,
            Response.Listener { response ->
                try {
                    holidaydetails = JSONObject(response).getJSONObject("response").getJSONArray("holidays")
                    recyclerView.adapter ?. notifyDataSetChanged()
//
                }
                catch (e : Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

                }

            }
            ,Response.ErrorListener{ error->


            })

        Volley.newRequestQueue(this).add(result)



    }




    inner class HolidayApadtor : RecyclerView.Adapter<HolidayView>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayView {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_holidayrecycle,parent,false)

            return HolidayView(view)
        }

        override fun getItemCount(): Int {
           return holidaydetails.length()
        }

        override fun onBindViewHolder(holder: HolidayView, position: Int) {
            try {
                holder.holidayname.text = holidaydetails.getJSONObject(position).getString("name")
                holder.holidaymonth.text = holidaydetails.getJSONObject(position).getJSONObject("date").getJSONObject("datetime").getString("month")
                holder.holidatdate.text = holidaydetails.getJSONObject(position).getJSONObject("date").getString("iso")
            }catch (e:java.lang.Error){


            }
        }


    }



    inner class HolidayView(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{


        val holidayname : TextView = itemView.findViewById(R.id.txtholiday)
        val holidaymonth : TextView = itemView.findViewById(R.id.txtmonth)
        val holidatdate : TextView = itemView.findViewById(R.id.txtdate)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val holiday = holidaydetails.getJSONObject(position)
                val intent = Intent(itemView.context, Holidaydetails::class.java)
              intent.putExtra("holiday", holiday.toString())

                var i = 0
                progressbar.visibility = VISIBLE
                i = progressbar.progress

                Thread(Runnable {

                    while (i < 100) {
                        i += 2
                        handler.post(Runnable {
                            progressbar.progress = i
                        })
                        try {
                            Thread.sleep(100)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }



                    progressbar.visibility = INVISIBLE
                }).start()
                itemView.context.startActivity(intent)


            }
        }


    }

}




