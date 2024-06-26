package com.example.jammah

//import androidx.annotation.NonNull
//import android.support.annotation.NonNull;

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.annotation.NonNull
import com.google.gson.Gson
import com.telr.mobile.sdk.activity.WebviewActivity
import com.telr.mobile.sdk.entity.request.payment.*
import com.telr.mobile.sdk.entity.response.payment.MobileResponse
import com.telr.mobile.sdk.entity.response.status.StatusResponse
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import java.math.BigInteger
import java.util.*


/** TelrPaymentGatewayPlugin */
class TelrPaymentGatewayPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var activity: Activity
  private lateinit var result: Result



  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "telr_payment_gateway")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    this.result = result
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "callTelRForTransaction") {
      var store_id : String = call.argument<String>("store_id")!!
      var key : String = call.argument<String>("key")!!
      var amount : String = call.argument<String>("amount")!!
      var app_install_id : String = call.argument<String>("app_install_id")!!
      var app_name : String = call.argument<String>("app_name")!!
      var app_user_id : String = call.argument<String>("app_user_id")!!
      var app_version : String = call.argument<String>("app_version")!!
      var sdk_version : String = call.argument<String>("sdk_version")!!
      var mode : String = call.argument<String>("mode")!!
      var tran_type : String = call.argument<String>("tran_type")!!
      var tran_cart_id : String = call.argument<String>("tran_cart_id")!!
      var desc : String = call.argument<String>("desc")!!
      var tran_language : String = call.argument<String>("tran_language")!!
      var tran_currency : String = call.argument<String>("tran_currency")!!
      var bill_city : String = call.argument<String>("bill_city")!!
      var bill_country : String = call.argument<String>("bill_country")!!
      var bill_region : String = call.argument<String>("bill_region")!!
      var bill_address : String = call.argument<String>("bill_address")!!
      var bill_first_name : String = call.argument<String>("bill_first_name")!!
      var bill_last_name : String = call.argument<String>("bill_last_name")!!
      var bill_title : String = call.argument<String>("bill_title")!!
      var bill_email : String = call.argument<String>("bill_email")!!
      var bill_phone : String = call.argument<String>("bill_phone")!!
        var token : String = call.argument<String>("token")!!
        var custref : String = call.argument<String>("customerRef")!!

      sendMessage(key,store_id,amount,app_install_id,app_name,app_user_id,app_version,sdk_version,mode,
              tran_type,tran_cart_id,desc,tran_language,tran_currency,bill_city,bill_country,bill_region,
              bill_address,bill_first_name,bill_last_name,bill_title,bill_email,bill_phone,custref,token)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  fun sendMessage(key:String,store_id:String,amount:String,app_install_id:String,app_name:String,
                  app_user_id:String,app_version:String,sdk_version:String,mode:String,tran_type:String,tran_cart_id:String,
                  desc:String,tran_language:String,tran_currency:String,bill_city:String,
                  bill_country:String,bill_region:String,bill_address:String,bill_first_name:String,
                  bill_last_name:String,bill_title:String,bill_email:String,bill_phone:String,custref:String, token: String,)
  {
    val intent = Intent(activity, WebviewActivity::class.java)
    intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequest(key,store_id,amount,app_install_id,app_name,app_user_id,app_version,sdk_version,mode,
            tran_type,tran_cart_id,desc,tran_language,tran_currency,bill_city,bill_country,bill_region,
            bill_address,bill_first_name,bill_last_name,bill_title,bill_email,bill_phone,custref,token))
      intent.putExtra(WebviewActivity.TOKENFLAG, token) // Pass the token to WebviewActivity
    intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.example.jammah.SuccessTransationActivity")
    intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.example.jammah.FailedTransationActivity")
    intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, true) 
    activity.startActivityForResult(intent, 100)
  } 
  
  fun getMobileRequest(
      key: String,
      store_id: String,
      amount: String,
      app_install_id: String,
      app_name: String,
      app_user_id: String,
      app_version: String,
      sdk_version: String,
      mode: String,
      tran_type: String,
      tran_cart_id: String,
      desc: String,
      tran_language: String,
      tran_currency: String,
      bill_city: String,
      bill_country: String,
      bill_region: String,
      bill_address: String,
      bill_first_name: String,
      bill_last_name: String,
      bill_title: String,
      bill_email: String,
      bill_phone: String,
      custref: String,
      token: String
  ) : MobileRequest {
    val mobile = MobileRequest()
//    mobile.setStore("25798") // Store ID
//    mobile.setKey("Nbsw5^mDR5@3m9Nc") // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.

    mobile.setStore(store_id) // Store ID
    mobile.setKey(key) // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
    mobile.setCustref(custref)
    val app = App()
    app.setId(app_install_id) // Application installation ID
    app.setName(app_name) // Application name
    app.setUser(app_user_id) // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
    app.setVersion(app_version) // Application version
    app.setSdk(sdk_version)
    mobile.setApp(app)

    val tran = Tran()
    tran.setTest(mode) // 1 - Test Mode | 0- Live Mode
    tran.setType(tran_type)
    tran.setClazz("paypage") // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
//    tran.setCartid(BigInteger(128, Random()).toString()) //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
    tran.setCartid(tran_cart_id) //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
    tran.setDescription(desc)
    tran.setLanguage(tran_language)
    tran.setCurrency(tran_currency)
    tran.setAmount(amount)
    mobile.setTran(tran)

    val billing = Billing()
    val address = Address()
    address.setCity(bill_city) // City : the minimum required details for a transaction to be processed
    address.setCountry(bill_country) // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
    address.setRegion(bill_region) // Region
    address.setLine1(bill_address) // Street address – line 1: the minimum required details for a transaction to be processed

    billing.setAddress(address)
    val name = Name()

    name.setFirst(bill_first_name) // Forename : .1the minimum required details for a transaction to be processed
    name.setLast(bill_last_name) // Surname : the minimum required details for a transaction to be processed
    name.setTitle(bill_title) // Title
    billing.setName(name)
    billing.setEmail(bill_email)
    billing.setPhone(bill_phone)

    mobile.setBilling(billing)



    return mobile
  }

  override fun onDetachedFromActivity() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity;
    binding.addActivityResultListener(this)
  }


  override fun onDetachedFromActivityForConfigChanges() {
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?): Boolean {
    Log.e("onActivityResult", "Method is Called" + requestCode);
    if (requestCode == 100 &&
            resultCode == Activity.RESULT_OK) {
      val paymentMethod: String = intent?.getStringExtra("auth")!!
      if (paymentMethod.equals("yes", ignoreCase = true)) {
        val status = intent.getParcelableExtra<Parcelable>(WebviewActivity.PAYMENT_RESPONSE) as MobileResponse?
    
        if (status?.auth != null) {
            val authData = HashMap<String, Any>()
            authData["status"] = status.getAuth().getStatus()
            authData["code"] = status.getAuth().getCode()
            authData["message"] = status.getAuth().getMessage()
            val gson = Gson()
            val jsonString = gson.toJson(authData)
            this.result.success(jsonString)
            Log.e("Transaction Ref failed", authData.toString())
        }
    } else {
        val status = intent.getParcelableExtra<Parcelable>(WebviewActivity.PAYMENT_RESPONSE) as StatusResponse?
        if (status?.auth != null) {
            val authData = HashMap<String, Any>()
            authData["status"] = status.getAuth().getStatus()
            authData["code"] = status.getAuth().getCode()
            authData["message"] = status.getAuth().getMessage()
            authData["tranref"] = status.getAuth().getTranref()
            val gson = Gson()
            val jsonString = gson.toJson(authData)
            this.result.success(jsonString)
            Log.e("Transaction complete", authData.toString())
        }
    }
      return  true
    }
    else if(requestCode == 100){
          val authData = HashMap<String, Any>()
          authData["status"] = "U"
          authData["code"] = "400"
          authData["message"] = "Unknown State"
          authData["tranref"] = "Null"
          val gson = Gson()
          val jsonString = gson.toJson(authData)
          this.result.success(jsonString)
      
    }
    return false
  }


}
