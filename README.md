# Android Text Manager 

Android Text Manager is a module you can integrate in your android app to change sring ressources remotly from your server.

### Installation:

1) Go to:
  
    File -> Import Module -> choose the folder "androidtextmanager".

2) add this to your gradle file :

	    compile project(':androidtextmanager')

### Usages:

String resources are absolutely static defined, you can't change their values. Tags can be useful to replace string text. 

1) First of all, we need to specify the ressource name in the tag, we must use the same name as "text" xml property.

        <TextView android:text="@string/hello_world" 
        	android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/poloText"
            android:tag="hello_world"
            />

2) we need to create a TextManager object and assign the activity context to a final var:

    	final TextManager textManager = TextManager.getInstance(); 
    	final Context currentContext = this;

    Then set a Listener that will watch for results, 
    
    	textManager.setOnRessourceLoaded(new RessourceLoadedListener() {
    		@Override
    		public void ressourcesLoaded() {
    			//Refresh all activity strings
    		    textManager.refreshViewTexts(currentContext, "en");
    		}
    
    		@Override
    		public void ressourcesLoadFailed() {
    			// do somethings here  
    		}
    	});

3) Now we just have to make a call to the server, call refreshRessources anywhere you want, generally on the launch of the application (OnCreate of the first activity):  
 	
	    textManager.refreshRessources(this, "https://exemple.com/mobile_string_ressources");

The method ressourcesLoaded will be triggered automatically when response from the server is stored. and ressourcesLoadFailed when the request fail.

4) The server side the root "https://exemple.com/mobile_string_ressources" must return a json with a structure as below:

    	[
    		{ 
    			language: "en",
    			strings: [
    				{ key: "hello_world", value: "Hi" },
    				{ key: "Logout", value: "Sig out" }
    			]
    		},
    		{ 
    			language: "fr",
    			strings: [
    				{ key: "hello_world", value: "Bonjour" },
    				{ key: "Logout", value: "se deconnecté" }
    			]
    		}
    	]

### Advenced usages:

We can Change specific texts not the whole activity texts:
    
        TextView textView = (TextView) findViewById(R.id.poloText);
        textView.setText(textManager.getString("hello_word", "en"));
