package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Adapter.MessageAdapter;
import com.example.myapplicationflyaway.Model.Message;
import com.example.myapplicationflyaway.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText message;
    ImageButton sendMessage;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    ImageButton imageButton_goBack;

    String userId, itineraryId;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recycler_view_chat);
        message = findViewById(R.id.message_edit_text_chat);
        sendMessage = findViewById(R.id.send_button);
        imageButton_goBack = findViewById(R.id.imageButton_goBack);

        messageList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

        itineraryId = getIntent().getExtras().getString("itineraryId");
        userId = getIntent().getExtras().getString("userId");

        imageButton_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, ItineraryPageActivity.class);
                i.putExtra("ItineraryId", itineraryId);
                i.putExtra("UserId", userId);
                startActivity(i);
                finish();
            }
        });


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = message.getText().toString().trim();
                if (question.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Digite algo", Toast.LENGTH_SHORT).show();
                } else {
                    addToChat(question, Message.SENT_BY_ME);
                    message.setText("");
                    callAPI(question);
                }
            }

        });
    }
     void addToChat(String message, String  sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response, Message.SENT_BY_BOT);
    }


    void callAPI(String question){

        messageList.add(new Message("Carregando...", Message.SENT_BY_BOT));

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("model", "gpt-3.5-turbo");
            JSONArray messageArr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role", "user");
            obj.put("content", question);
            messageArr.put(obj);

            jsonObject.put("messages", messageArr);
            jsonObject.put("temperature", 0);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Erro em gerar resposta por: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject1.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                   addResponse("Erro em gerar resposta por: " + response.body().string());
                }
            }
        });

    }
}