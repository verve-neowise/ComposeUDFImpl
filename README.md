# Android Compose UDF Implementation

![Work](screenshots/UDF.jpg)

**State** - Состояние UI

**Action** - действия отправляемые в Reducer (ViewModel)

**Event** - события для UI  (не меняет State, можно использовать для событий навигации)

**Effect** - Побочные эффекты (Асинхронные, Многопоточные операции), вроде отправки запросов на сервер и тп (не меняет State)

**Reducer** (ViewModel, метод dispatch) - Получает действия (Action) с UI или Platform (Android System) или Effector. Передает Action и текущий State в reduce. Reducer тем самым обновляет State на полученный и запускает полученные эффекты передав в Effector

**Effector** - рантайм эффектов, запускает полученные эффекты асинхронно. После завершения эффект может вернуть Action, который передается в Reducer для обработки

**reduce** - получает Action и State, синхронно применяет action к state и возращает обновленный State и список Side Effect'ов в Reducer. Так же может производить events

Activity
```kotlin
viewModel.dispatch(Action.LoadContent)
```

ViewModel
```kotlin
fun reduce(action: Action, state: State): Pair<State, Set<Effect>> = when(action) {
    is Action.LoadContent -> {
        state.copy(isLoading = true) with Effect.FetchData
    }
    is Action.UpdateContent -> {
        state.copy(isLoading = false, content = action.content)
    }
}
```
```kotlin
fun affect(effect: Effect): Flow<Action?> = flow {
    when(effect) {
        is Effect.FetchData -> {
            fetchDataUseCase().collect { content ->
                emit(UpdateContent(content))
            }
        }
    }
}
```

UI

Compose

```kotlin
@Composable 
fun UI(
    state: State,
    events: Events,
    dispatch: (Action) -> Unit
) {

    val context = LocalContext.current

    EventEffect(events) { event ->
        when(event) {
            is ShowMessage -> showToast(context, event.message)
        }
    }

    UserProfile(
        user = state.user,
        isLoading = state.isLoading
    )
    
    DetailContent(
        content = state.content
    )

    Button(
        onClick = {
            dispatch(Action.LoadContent)
        }
    ) {
        Text("Load Details")
    }
}
```