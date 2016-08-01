# RoundProgressTextView

TextView with Round Pogress.

## Screenshot
<img src="https://raw.githubusercontent.com/hearsilent/RoundProgressTextView/master/screenshots/screenrecord.gif" height="500">
<img src="https://raw.githubusercontent.com/hearsilent/RoundProgressTextView/master/screenshots/layout-2016-02-29-172603.png" height="500">

# Usage

If you want change progress value programmatically call:
```java
setProgress(int progress)
```
Else if you want change progress but not in UI thread, then call:
```java
setProgressNotInUiThread(int progress)
```


# Customization

You can customize to what you want.
```java
<hearsilent.roundprogresstextview.RoundProgressTextView
	android:layout_width="200sp"
	android:layout_height="40sp"
	android:gravity="center"
	android:text="HearSilent"
	android:textSize="17sp"
	app:max="100"
	app:progress="35"
	app:progress_color="#1e88e5"
	app:progress_stoke_width="2.5"
	app:stoke_width="1"/>
```

## Compatibility

Android GINGERBREAD 2.3+

### Let me know!

I'd be really happy if you sent me links to your projects where you use my component. Just send an email to hear.silent1995@gmail.com And do let me know if you have any questions or suggestion regarding the example. 

## License

    Copyright 2016, HearSilent

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
