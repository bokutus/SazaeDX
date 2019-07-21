package com.example.janken2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_result2.*

class ResultActivity2 : AppCompatActivity() {

    val gu = 0//グーチョキパーの定数を定義している
    val choki = 1
    val pa = 2

    override fun onCreate(savedInstanceState: Bundle?) {//overrideでonCreateを上書きして、内容を追加している
        super.onCreate(savedInstanceState)//onCreateを引き継いでいる
        setContentView(R.layout.activity_result2)//setContentViewは、activity_result2.xmにというレイアウトファイルに画面レイアウトを設定して読み込ませるという記述
        //上記のコードは、onCreate()が呼び出されたら（アクティビティ開始されたら最初に）activity_result2.xml（レイアウトファイル）を読み込んで画面に何を表示するか決める
        val id = intent.getIntExtra("MY_HAND", 0)
        /*MainActivity.ktで入力された(タップされた)手（値）を
        上記のgetIntExtraから引き出して、下記の式に値をInt型に変換してから当てはめている
        */

        val myHand: Int//Int型のmyHandを用意する
        myHand = when (id) {//when式で戻り値の値を取得している
            R.id.gu -> {//when式の内部では返す値を定数として追加している
                myHandImage.setImageResource(R.drawable.gu)
                gu
                //activity_result2.xmlのmyHandImageにguの画像をセットしている
            }
            R.id.choki -> {
                myHandImage.setImageResource(R.drawable.choki)
                choki
            }
            R.id.pa -> {
                myHandImage.setImageResource(R.drawable.pa)
                pa
            }
            else -> gu//戻り値がnullにならないよう、elseを追加している
        }
        //コンピューターの手を決める
        val comHand = getHand()
        when (comHand) {
            gu -> comHandImage.setImageResource(R.drawable.sazae_gu)
            //0ならグーがでて、activity_result2.xmlのcomHandImageにsazae_guの画像をセットしている
            choki -> comHandImage.setImageResource(R.drawable.sazae_choki)
            pa -> comHandImage.setImageResource(R.drawable.sazae_pa)
        }
        //勝敗を決める
        val gameResult = (comHand - myHand + 3) % 3
        when (gameResult) {
            0 -> resultLabel.setText(R.string.result_draw) //引き分け
            1 -> resultLabel.setText(R.string.result_win)//勝ち
            2 -> resultLabel.setText(R.string.result_lose)//負け
        }
        backButton.setOnClickListener { finish() }

        //ジャンケンの結果を保存する
        saveData(myHand, comHand, gameResult)
    }
    private fun saveData(myHand: Int, comHand: Int, gameResult: Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUUNT", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val lastGameResult = pref.getInt("GAME_RESULT", -1)

        val edtWinningStreakCount: Int =
                when{
                    lastGameResult == 2 && gameResult == 2 ->
                        winningStreakCount + 1
                    else ->
                        0
                }
        val editor = pref.edit()
        editor.putInt("GAME_COUNT", gameCount + 1)
            .putInt("WINNING_STREAK_COUNT", edtWinningStreakCount)
            .putInt("LAST_MY_HAND", myHand)
            .putInt("LAST_COM_HAND", comHand)
            .putInt("BEFORE_LAST_COM_HAND", lastComHand)
            .putInt("GAME_RESULT", gameResult)
            .apply()
    }
    private fun getHand(): Int {
        var hand = (Math.random() * 3).toInt()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        val lastMyHand = pref.getInt("LAST_MY_HAND", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val beforeLastComHand = pref.getInt("BEFORE_LAST_COM_HAND", 0)
        val gameResult = pref.getInt("GAME_RESULT", -1)

        if (gameCount == 1){
            if (gameResult == 2){
                //今回の勝負が一回目で、コンピューターが勝った場合、
                //コンピューターは次に出す手を変える
                while (lastComHand == hand) {
                    hand = (Math.random() * 3).toInt()
                }
            }else if (gameResult == 1) {
                //前回の勝負が一回目で、コンピューターが負けた場合、
                //相手の出した手に勝つ手を出す
                hand = (lastMyHand - 1 + 3) % 3
            }
        }else if (winningStreakCount > 0)
            if(beforeLastComHand == lastComHand){
                //同じ手で連勝した場合は出す手を変える
                while (lastComHand == hand) {
                    hand == (Math.random() * 3).toInt()
                }
            }
        return hand
    }
}
