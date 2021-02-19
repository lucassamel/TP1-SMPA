package lucassamel.br.tp1_smpa

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LocationListener {


    val COARSE_REQUEST = 12345
    val FINE_REQUEST = 54321

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnObter = btnCoordenadas
        btnObter.setOnClickListener {
            carregarCordenadas()
        }
    }


    private fun carregarCordenadas(){

        Log.i("TP1", "Iniciando Carregador de coordenadas.")
        try {
            val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
            val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            var location: Location? = null
            var latitude = 0.0
            var longitude = 0.0

            if(isNetEnable){
                Log.i("TP1", "Obtendo localizacao pela rede")
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            2000L,
                            0F,
                            this
                    )
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null){
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                }
            }else{
                Log.i("TP1", "Rede não habilitada para localizacao")
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), COARSE_REQUEST)
            }


            if(isGpsEnable){
                Log.i("TP1", "Obtendo localizacao pelo GPS")
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            2000L,
                            0F,
                            this
                    )
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location != null){
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                }
            }else{
                Log.i("TP1", "GPS nao habilitado para a localizacao")
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_REQUEST)
            }

            val lblValorLatitude = lblValorLatitude
            lblValorLatitude.setText(latitude.toString())
            val lblValorLongitude = lblValorLongitude
            lblValorLongitude.setText(longitude.toString())

        }catch (ex: SecurityException){

            Log.i("TP1", "Erro na localizacao ")
        }
        Log.i("TP1", "Fim no carregamento das coordenadas")
    }

    override fun onLocationChanged(location: Location) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

}