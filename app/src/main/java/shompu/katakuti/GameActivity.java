package shompu.katakuti;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class GameActivity extends Activity implements OnClickListener {
	
	private static Button[][] b=new Button[3][3];
	private static ArrayList<Button> ab= new ArrayList<>();
	private boolean win=false,block=false;
	private boolean user,hard=true,com;
	private int u,c;
	private Handler loop=new Handler();
	private Runnable game,U;
	private SharedPreferences sp;
	private Dialog d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game);

        b[0][0]=findViewById(R.id.one);
		b[0][1]=findViewById(R.id.two);
		b[0][2]=findViewById(R.id.three);
		b[1][0]=findViewById(R.id.four);
		b[1][1]=findViewById(R.id.five);
		b[1][2]=findViewById(R.id.six);
		b[2][0]=findViewById(R.id.seven);
		b[2][1]=findViewById(R.id.eight);
		b[2][2]=findViewById(R.id.nine);

		 for (int i=0;i<3;i++) {
	        	for(int j=0;j<3;j++) {
	        		b[i][j].setOnClickListener(this);
	        		b[i][j].setTypeface(Typeface.DEFAULT_BOLD);
	        		b[i][j].setTextSize(70);
	        	}
	        }
		 
		sp=getPreferences(MODE_PRIVATE);
		u=sp.getInt("user", 0);
		c=sp.getInt("com",0);
		
		TextView score=findViewById(R.id.score);
		score.setTextColor(Color.BLUE);
		score.setTextSize(20);
		score.setGravity(Gravity.CENTER);
		score.setTypeface(Typeface.SANS_SERIF);
		score.setText("You: "+u+"\tComputer: "+c);

		ab.clear();
		for(int i=0;i<3;i++){
			ab.addAll(Arrays.asList(b[i]).subList(0, 3));
		}
		
		win=false;block=false;hard=true;
		user=true;com=false;
		
		new AlertDialog.Builder(this).setTitle("First Turn")
		.setMessage("Who will give first turn?")
		.setPositiveButton("Me", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				user=true;com=false;
			}
		}).setNegativeButton("Computer", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				user=false;com=true;
			}
		})
		.setCancelable(false).show();

		 U=new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println("USER MODE user="+user+"Com="+com);
				if(user)
					loop.postDelayed(this, 1000);
				else {
					com=true;
					block=false;
					loop.removeCallbacks(this);
				}
			}
		};
		
		 game=new Runnable() {
			
			@Override
			public void run() {
				
				
					user_turn();
					
					if(wincheck()){
						
						d =new Dialog(GameActivity.this);
						d.requestWindowFeature(Window.FEATURE_NO_TITLE);
						d.setContentView(R.layout.my_dialog);
						d.setCancelable(false);

						ImageView logo= d.findViewById(R.id.imageView1);
						logo.setImageResource(R.drawable.ic_winner);

						Button b= d.findViewById(R.id.bb2);
						b.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								loop.removeCallbacks(U);
								loop.removeCallbacks(game);
								
								SharedPreferences.Editor e =sp.edit();
								e.putInt("user",u+1);
								e.apply();
								d.dismiss();
								finish();
								
							}
						});
						Sounds.playSound(Sounds.PLAYER_WIN);
						d.show();
						
						return;
					}
					
					if(draw()){
						d =new Dialog(GameActivity.this);
						d.requestWindowFeature(Window.FEATURE_NO_TITLE);
						d.setContentView(R.layout.my_dialog);
						d.setCancelable(false);

						ImageView logo= d.findViewById(R.id.imageView1);
						logo.setImageResource(R.drawable.ic_drawgame);

						Button b= d.findViewById(R.id.bb2);
						b.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
							loop.removeCallbacks(U);
								loop.removeCallbacks(game);
								d.dismiss();
								finish();	
							}
						});
						Sounds.playSound(Sounds.DRAW);
						d.show();

						return;
					}

				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if(com)
						com_turn();

					if(draw()){
						d =new Dialog(GameActivity.this);
						d.requestWindowFeature(Window.FEATURE_NO_TITLE);
						d.setContentView(R.layout.my_dialog);
						d.setCancelable(false);

						ImageView logo= d.findViewById(R.id.imageView1);
						logo.setImageResource(R.drawable.ic_drawgame);

						Button b= d.findViewById(R.id.bb2);
						b.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
							loop.removeCallbacks(U);
								loop.removeCallbacks(game);
								d.dismiss();
								finish();
							}
						});
						Sounds.playSound(Sounds.DRAW);
						d.show();
						
						return;
					}
				loop.postDelayed(this, 1000);
			}

			private void com_turn() {
				//Com_Sound_player.start();
				Sounds.playSound(Sounds.COMPUTER_TURN);

				if(com){
					wintry(b[0][0],b[0][1],b[0][2]);
					wintry(b[1][0],b[1][1],b[1][2]);
					wintry(b[2][0],b[2][1],b[2][2]);
					wintry(b[0][0],b[1][0],b[2][0]);
					wintry(b[0][1],b[1][1],b[2][1]);
					wintry(b[0][2],b[1][2],b[2][2]);
					wintry(b[0][0],b[1][1],b[2][2]);
					wintry(b[0][2],b[1][1],b[2][0]);
					
					if(!win){
						//Toast.makeText(GameActivity.this, "Computer Waiting..."+"win "+win+"block "+block+"com "+com, Toast.LENGTH_SHORT).show();
		
						if(hard && !block){
							block_opponent();
						}
						if(com) {
							//Toast.makeText(GameActivity.this, "Still Not Blocked", Toast.LENGTH_SHORT).show();
							int lok=0;
							if(ab.size()!=0)
							lok=(int)Math.abs(Math.random()*10000000)%ab.size();
							System.out.println(" 			 "+lok+" 			 "+ab.size());
							Button ff=ab.get(lok);
							ff.setText("O");
							ab.remove(lok);
							//User_Sound_player.stop();
							//com=false;
						}
					}
					
					com=false;
					user=true;
				}
				
				
			}

			private void user_turn() {
				
				System.out.println("User Turn");
				

				loop.post(U);
				
			}
		};
			
		loop.post(game);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	@Override
	public void onClick(View V) {
		//User_Sound_player.start();
		Button bb=(Button)V;
		if(bb.getText().equals("")&&user){
			Sounds.playSound(Sounds.PLAYER_TURN);

			bb.setTextColor(Color.RED);
			bb.setText("X");
			//bb.setTextColor(Color.BLUE);
			ab.remove(bb);
			user=false;
			block=false;
			com=true;
			//System.out.println("Button Clicked. user="+user);
		}
	}
	
    void wintry(Button b1,Button b2,Button b3) {
		if(!win){
			if(b1.getText().equals("O")&&b2.getText().equals("O")&&b3.getText().equals("")){
				b3.setText("O");				
				d =new Dialog(GameActivity.this);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.my_dialog);
				d.setCancelable(false);

				ImageView logo= d.findViewById(R.id.imageView1);
				logo.setImageResource(R.drawable.ic_loser);

				Button b= d.findViewById(R.id.bb2);
				b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						loop.removeCallbacks(U);
				loop.removeCallbacks(game);
				SharedPreferences.Editor e =sp.edit();
				e.putInt("com",c+1);
				e.apply();
				d.dismiss();
				finish();
						
					}
				});
				d.show();
				
				win=true;
				com=false;
				return;
			}
			if(b1.getText().equals("O")&&b2.getText().equals("")&&b3.getText().equals("O")){
				b2.setText("O");
				d =new Dialog(GameActivity.this);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.my_dialog);
				d.setCancelable(false);

				ImageView logo= d.findViewById(R.id.imageView1);
				logo.setImageResource(R.drawable.ic_loser);

				Button b= d.findViewById(R.id.bb2);
				b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						loop.removeCallbacks(U);
				loop.removeCallbacks(game);
				SharedPreferences.Editor e =sp.edit();
				e.putInt("com",c+1);
				e.apply();
				d.dismiss();
				finish();
					}
				});
				Sounds.playSound(Sounds.COMPUTER_WIN);

				d.show();

				win=true;
				com=false;
				return;
			}
			if(b1.getText().equals("")&&b2.getText().equals("O")&&b3.getText().equals("O")){
				b1.setText("O");
				d =new Dialog(GameActivity.this);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.my_dialog);
				d.setCancelable(false);

				ImageView logo=d.findViewById(R.id.imageView1);
				logo.setImageResource(R.drawable.ic_loser);

				Button b=d.findViewById(R.id.bb2);
				b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						loop.removeCallbacks(U);
				loop.removeCallbacks(game);
				SharedPreferences.Editor e =sp.edit();
				e.putInt("com",c+1);
				e.apply();
				d.dismiss();
				finish();

					}
				});
				Sounds.playSound(Sounds.COMPUTER_WIN);

				d.show();
				
				win=true;
				com=false;
			}
		}
	}
	
	 boolean draw(){
		
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(b[i][j].getText().equals(""))
					return false;
			}
		}
		return true;
	}
	
	 boolean wincheck(){

		//System.out.println("Checkin com win");

		if(b[0][0].getText().equals("X")&&b[0][1].getText().equals("X")&&b[0][2].getText().equals("X")){
			return true;
		}
		if(b[1][0].getText().equals("X")&&b[1][1].getText().equals("X")&&b[1][2].getText().equals("X")){
			return true;
		}
		if(b[2][0].getText().equals("X")&&b[2][1].getText().equals("X")&&b[2][2].getText().equals("X")){
			return true;
		}
		if(b[0][0].getText().equals("X")&&b[1][0].getText().equals("X")&&b[2][0].getText().equals("X")){
			return true;
		}
		if(b[0][1].getText().equals("X")&&b[1][1].getText().equals("X")&&b[2][1].getText().equals("X")){
			return true;
		}
		if(b[0][2].getText().equals("X")&&b[1][2].getText().equals("X")&&b[2][2].getText().equals("X")){
			return true;
		}
		if(b[0][0].getText().equals("X")&&b[1][1].getText().equals("X")&&b[2][2].getText().equals("X")){
			return true;
		}
		if(b[0][2].getText().equals("X")&&b[1][1].getText().equals("X")&&b[2][0].getText().equals("X")){
			return true;
		}
		return false;
	}
	
	
	 void block_opponent(){
		//System.out.println("b[0][0]  ,b[0][1]  b[0][2]");
		blocktry(b[0][0],b[0][1],b[0][2]);
		//System.out.println("b[1][0], b[1][1], b[1][2]");
		blocktry(b[1][0],b[1][1],b[1][2]);
		//System.out.println("b[2][0], b[2][1], b[2][2]");
		blocktry(b[2][0],b[2][1],b[2][2]);
		//System.out.println("b[0][0], b[1][0], b[2][0]");
		blocktry(b[0][0],b[1][0],b[2][0]);
		//System.out.println("b[0][1], b[1][1], b[2][1]");
		blocktry(b[0][1],b[1][1],b[2][1]);
		//System.out.println("b[0][2], b[1][2], b[2][2]");
		blocktry(b[0][2],b[1][2],b[2][2]);
		//System.out.println("b[0][0], b[1][1], b[2][2]");
		blocktry(b[0][0],b[1][1],b[2][2]);
		//System.out.println("b[0][2], b[1][1], b[2][0]");
		blocktry(b[0][2],b[1][1],b[2][0]);
		//Toast.makeText(this, "==Block state==", Toast.LENGTH_SHORT).show();
	}
	
	
	 void blocktry(Button b1,Button b2,Button b3) {

		if(!block){
			if(b1.getText().equals("X")&&b2.getText().equals("X")&&b3.getText().equals("")){
				b3.setText("O");
				
				ab.remove(b3);
				block=true;
				com=false;
				return;
			}
			if(b1.getText().equals("X")&&b2.getText().equals("")&&b3.getText().equals("X")){
				b2.setText("O");
				
				ab.remove(b2);
				block=true;
				com=false;
				return;
			}
			if(b1.getText().equals("")&&b2.getText().equals("X")&&b3.getText().equals("X")){
				b1.setText("O");
					
				ab.remove(b1);
				block=true;
				com=false;
			}
		}
	}

	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		loop.removeCallbacks(U);
		loop.removeCallbacks(game);
		finish();
	}
	 

}
