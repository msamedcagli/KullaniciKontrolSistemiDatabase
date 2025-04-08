package com.msamedcagli.delsaappson

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var itemNameEditText: EditText
    private lateinit var itemImageView: ImageView
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var currentItem: Item

    private lateinit var dao: ItemDAO
    private lateinit var db: ItemDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        db = ItemDataBase.getDatabase(this)
        dao = db.itemdao()

        backButton = findViewById(R.id.backButton)
        itemNameEditText = findViewById(R.id.itemNameEditText)
        itemImageView = findViewById(R.id.itemImageView)
        deleteButton = findViewById(R.id.deleteButton)
        saveButton = findViewById(R.id.saveButton)

        backButton.setOnClickListener {
            onBackPressed()
        }

        val isNewItem = intent.getBooleanExtra("isNewItem", false)

        if (!isNewItem) {
            try {
                currentItem = intent.getParcelableExtra<Item>("item") ?: throw IllegalStateException("Item not found")
                itemNameEditText.setText(currentItem.name)
                itemImageView.setImageResource(currentItem.imageResId)
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        } else {
            // Yeni item ekleme modu: varsayılan görsel ayarla
            itemImageView.setImageResource(R.drawable.cihaz)
        }

        saveButton.setOnClickListener {
            val newName = itemNameEditText.text.toString()
            if (newName.isNotEmpty()) {
                lifecycleScope.launch {
                    if (isNewItem) {
                        // Yeni cihaz ekleniyor (veritabanına insert!)
                        val newItem = Item(name = newName, imageResId = R.drawable.cihaz)
                        val newId = dao.insert(newItem)
                        val insertedItem = newItem.copy(id = newId.toInt())
                        Toast.makeText(this@DetailActivity, "Kayıt eklendi: ID=$newId", Toast.LENGTH_SHORT).show()
                        val resultIntent = Intent().putExtra("item", insertedItem)
                        setResult(RESULT_OK, resultIntent)
                    } else {
                        // Mevcut cihaz güncelleniyor
                        val updatedItem = currentItem.copy(name = newName)
                        dao.update(updatedItem)
                        val resultIntent = Intent().putExtra("item", updatedItem)
                        setResult(RESULT_OK, resultIntent)
                    }
                    finish()
                }
            } else {
                Toast.makeText(this, "Item name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                dao.delete(currentItem)  // Room ile sil
                val resultIntent = Intent().putExtra("item", currentItem)
                setResult(RESULT_CANCELED, resultIntent)
                finish()
            }
        }
    }
}
