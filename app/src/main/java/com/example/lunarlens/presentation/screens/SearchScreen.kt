package com.example.lunarlens.presentation.screens
import com.example.lunarlens.utils.Search
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.lunarlens.data.remote.api.search
import com.example.lunarlens.data.remote.dto.Item
import com.example.lunarlens.presentation.viewmodels.homeScreenViewmodel
import androidx.compose.runtime.DisposableEffect
import com.example.lunarlens.utils.Screens

@Composable
fun searchScreen( navController: NavController, homeScreenViewmodel: homeScreenViewmodel)
{
    val searchresult  = homeScreenViewmodel.searchResult.collectAsState()
    val searchField = homeScreenViewmodel.searchField.collectAsState()
    val x = searchresult.value
    DisposableEffect(Unit) {
        homeScreenViewmodel.setBottomBar(false)
        onDispose {
            homeScreenViewmodel.setBottomBar(true)
        }

    }
    LazyColumn(Modifier.systemBarsPadding().padding(end = 16.dp , start = 16.dp , bottom = 16.dp)){
        item()
        {
            Row(Modifier.fillMaxSize() , horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                OutlinedTextField(
                    leadingIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()


                    }){
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null ,
                        tint = MaterialTheme.colorScheme.primary)
                }},
                    label= {
                        Text("Search",
                            style = MaterialTheme.typography.bodyMedium)},
                    value = searchField.value,
                    onValueChange =
                    {
                        homeScreenViewmodel.updateSearch(it)
                    } ,
                    modifier = Modifier,
                    colors =TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    )
                IconButton(onClick = {
                    homeScreenViewmodel.updateSearch(searchField.value)
                    homeScreenViewmodel.getSearchResult()
                }) {
                    Icon(
                        imageVector = Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        items(searchresult.value.items)
        { items ->
            searchItem(items, navController)
        }

    }
}


@Composable
fun searchItem(item : Item , navController: NavController)
{
    val base  = "https://images-assets.nasa.gov/image/"
    val add = item.href.substring(37,46)
    val final = item.href.substring(37,45)+"~orig.jpg"
    val finalLink = base+add+final
    Card(Modifier.fillMaxWidth().padding(bottom = 8 .dp)
        .clickable(onClick = {navController
            .navigate(Screens.DetailsPage(hdurl = finalLink , title = item.data[0].title,
                description = item.data[0].description , thumb = item.links[0].href ))})) {
        Row(Modifier.fillMaxWidth()) {
            AsyncImage(model = item.links[0].href , contentDescription = null ,
                modifier = Modifier.width(100.dp).height(100.dp),
                contentScale = ContentScale.Crop)
            Column(horizontalAlignment = Alignment.Start , modifier = Modifier.padding(8.dp)) {
                Text(item.data[0].title.toString() , style = MaterialTheme.typography.titleSmall)
                Text(item.data[0].date_created.substring(0,10))
            }
        }

    }

}