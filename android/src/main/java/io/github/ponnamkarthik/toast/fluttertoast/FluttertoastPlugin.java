package io.github.ponnamkarthik.toast.fluttertoast;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/** FluttertoastPlugin */
public class FluttertoastPlugin implements MethodCallHandler {

  Context ctx;

  int defaultTextColor = Color.TRANSPARENT;

  FluttertoastPlugin(Context context) {
    ctx = context;
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "PonnamKarthik/fluttertoast");
    channel.setMethodCallHandler(new FluttertoastPlugin(registrar.context()));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("showToast")) {
      String msg  = call.argument("msg").toString();
      String length = call.argument("length").toString();
      String gravity = call.argument("gravity").toString();
      Long bgcolor = call.argument("bgcolor");
      Long textcolor = call.argument("textcolor");

      Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);

      toast.setText(msg);

      if(length.equals("long")) {
        toast.setDuration(Toast.LENGTH_LONG);
      } else {
        toast.setDuration(Toast.LENGTH_SHORT);
      }

      switch (gravity) {
          case "top":
              toast.setGravity(Gravity.TOP, 0, 100);
              break;
          case "center":
              toast.setGravity(Gravity.CENTER, 0, 0);
              break;
          default:
              toast.setGravity(Gravity.BOTTOM, 0, 100);
        }

      TextView text = toast.getView().findViewById(android.R.id.message);

      if (defaultTextColor == 0) {
          defaultTextColor = text.getCurrentTextColor();
      }

      try {
          RoundRectShape rectShape = new RoundRectShape(new float[] {100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f}, null, null);

          ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);
          shapeDrawable.getPaint().setColor(bgcolor != null ? bgcolor.intValue() : Color.BLACK);
          shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
          shapeDrawable.getPaint().setAntiAlias(true);
          shapeDrawable.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);

        
          toast.getView().setBackgroundDrawable(shapeDrawable);
          //if (android.os.Build.VERSION.SDK_INT <= 27) {
          //    toast.getView().setBackground(shapeDrawable);
          //} else {
          //    text.setBackground(shapeDrawable);
          //}
      } catch (Exception e) {
          e.printStackTrace();
      }

      try {
          text.setTextColor(textcolor != null ? textcolor.intValue() : defaultTextColor);
      } catch (Exception e) {
          e.printStackTrace();
      }

      toast.show();

      result.success("Success");

    } else {
      result.notImplemented();
    }
  }
}
