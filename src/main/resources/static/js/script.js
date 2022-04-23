$(function(){

	showTime();

	setInterval(function(){
	    showTime();
	    },
	60000);

	setInterval(function(){
	    refreshPage();
	    },
	600000);

    addUrgencyAttributeToTasks();

    markFiltersWithActiveTasks();

    displayAllTasks();


	 //Getting task
    $(document).on('click', '.task-link', function(){
        let link = $(this);
        // let taskId = link.data('id');
        let task = $(link).parents('.task-item');
        enableCorrespondingFilter(task);
        let update_form = $(link).parent().children('.update-task-form');
        let deadline_span = $(task).children('.deadline_span');
        $(update_form).toggleClass('hidden');
            if($(update_form).hasClass('hidden')){
                $(deadline_span).show();
            } else{
                $(deadline_span).hide();
            }
        return false;
    });

   

    //displaying tasks by urgency filters
    $(document).on('click', '.filter-by-urgency', function(){
        let filter = $(this);
    	let urgency = $(filter).data('urgency');
    	filterTasks(filter, urgency);
    });

    //show all tasks filter
    $('#all').on('click', function(){
        let tasks = $('.task-list').children('.task-item');
        $(this).siblings().removeClass('chosen');
        $(this).addClass('chosen');
        for(let i = 0; i <  tasks.length; i++){
            let task = tasks[i];
            $(task).show();
            isUrgent(task);
            }
        }
    );


    //showing add-task form
    $('#add-new-task-button').on('click', function(){
        $('#add-task-page').toggleClass('hidden');
        if($('#add-task-page').hasClass('hidden')){
            $(this).text('Новое дело');
        } else {
            $(this).text('Свернуть');
        }
    });

	 //saving a new task to the list
	$('#save-button').on( 'click', function(){

	    if($('#name').val() == ''){
	    alert("Заполните поле Название");
	    return false;
	    }

        if($('#deadline').attr('min') > $('#deadline').val()) {
            alert("Значение календаря должно быть больше " + $('#deadline').attr('min'));
            return false;
        }

	    else {
        let data = $('#add-task-form').serialize();

        $.ajax({
            method: "POST",
            url: '/tasks/',
            data: data,
            success: function(response){
                $('#add-task-form').css('display', 'none');
                let task = data;
                task.id = response;
                let dataArray = $('#add-task-form form').serializeArray();
                for(i in dataArray) {
                    task[dataArray[i]['name']] = dataArray[i]['value'];
                }
                window.location.reload();
            }
        });
        return false;
        $('#home-page').css('display', 'block');
        $('#add-task-page').hide();
        }
	});


    //updating one task
	$(document).on( 'click', '.update-button', function(){
        let task_id = $(this).parents('.task-item').data('id');
        let data = $(this).parents('.update-task-form').serialize();
        let update_form = $(this).parents('.update-task-form')
        let task_deadline = $(update_form).next();

        if($('.name-upd').val() == ''){
            alert("Заполните поле Название");
            return false;
        }

        let current = new Date();
        if(current > Date.parse($('.deadline-upd').val())) {
            alert("Значение календаря должно быть больше " + current);
            return false;
        }

        else{
            $.ajax({
                method: "POST",
                url: '/tasks/' + task_id ,
                data: data,
                success: function(response){
                    let dataArray = $('.update-task-form form').serializeArray();
                     $(update_form).toggleClass('hidden');
                     $(task_deadline).show();
                     window.location.reload();
                },
                error : function(response){
                if (response.status == 404)
                    alert('Задание не найдено!')
                }
            });
            return false;
        }
    });

    //Deleting one task
    $(document).on('click','.delete-button', function(){
        if(confirm("Вы действительно хотите отметить это задание как завершённое?")){
            let link = $(this).parents('.task-name-container').children('.task-link');
            let taskId= $(this).data('id');
            $.ajax({
                method: "DELETE",
                url: '/tasks/' + taskId,
                success: function(){
                    link.parents('.task-item').remove();
                    window.location.reload();
                },
                error: function(response){
                if(response.status == 404) {
                    alert('Список заданий пуст');
                }
            }
        });
        }else alert("Действие отменено");
    });

    //Deleting all tasks
    $('#delete-all-tasks-button').click(function(){
        if(confirm("Вы действительно хотите отметить все задачи как завершённые?")){
            $.ajax({
                method: "DELETE",
                url: '/tasks/',
                success: function(response){
                    $('.task-list').children().remove();
                    window.location.reload();
                },
                error: function(response){
                    if(response.status == 404) {
                        alert('Список заданий пуст');
                    }
                }
            });
        }
        else alert("Действие отменено");
    });



	//change the add new task button style
    $('#add-new-task-button').hover(
        function(){
            $(this).css('background-color', '#2b2b86');
        },
        function(){
            $(this).css('background-color', '#5353d8');
    });

	//change delete all button style
    $('#delete-all-tasks-button').hover(
        function(){
            $(this).css('background-color', '#5f1aab');
        },
        function(){
            $(this).css('background-color', '#8c32ef');
        }
    );


    //change save new task button style
    $('#save-button').hover(function(){
        $(this).css('background-color', '#2b2b86');
    },
    function(){
        $(this).css('background-color', '#5353d8');
    });

    $('.update-button').hover(function(){
        $(this).css('background-color', '#308c55');
        },
    function(){
        $(this).css('background-color', '#53d085fa');
    });

    //underline chosen filter button
    $(document).on('click', ".filter-by-urgency", function(){
        $(this).siblings().removeClass('chosen');
        $(this).addClass('chosen');
    })
});

//updating main page
function refreshPage(){
    window.location.reload();

}

//converting time to a readable format
function getTimeReadableFormat(deadline){
    let d = new Date(deadline);
    let datestring = numToStr(d.getHours()) + ':'+ numToStr(d.getMinutes()) +' ' + numToStr(d.getDate()) + '/' +
            numToStr((d.getMonth() + 1)) + '/' + d.getFullYear();
        return datestring;
}


//parsing one-digit date
function numToStr(value){
    value += "";
    if(value.length == 1) return "0" + value;
    else return value;
}

//showing clock on the page
function showTime() {
    let timerId;
        let d = new Date();
        let days = [ "Воскресенье", "Понедельник", "Вторник", "Среда",
                     "Четверг", "Пятница", "Суббота" ];
        let months = [ "января", "февраля", "марта", "апреля", "мая",
                       "июня", "июля", "августа", "сентября", "октября",
                       "ноября", "декабря" ];
        let msg = days[d.getDay()] + " ";
        msg += d.getDate() + " ";
        msg += months[d.getMonth()] + " ";
        msg += d.getFullYear() + " ";
        msg += numToStr(d.getHours()) + ":";
        msg += numToStr(d.getMinutes());
        $('#clock').text(msg);
}

function calculateUrgencyForTasks(task){

    let timeUnits = new Map();
    timeUnits.set('hour', (60 * 60 * 1000));
    timeUnits.set('day', (60 * 60 * 1000 * 24));
    timeUnits.set('week', (60 * 60 * 1000 * 24 * 7));
    timeUnits.set('month', (60 * 60 * 1000 * 24 * 30));

    let urgency = '';
    let deadlineString = $(task).data('deadline');
    let deadline = Date.parse(deadlineString);
    let ldt = new Date();
    if(deadline < ldt){
        urgency = 'yesterday';
    }
    else if(deadline > ldt.getTime() && deadline < (ldt.getTime() + timeUnits.get('hour'))){
        urgency = 'hour';
    }
    else if(deadline > (ldt.getTime() + timeUnits.get('hour')) &&
        deadline < (ldt.getTime() + timeUnits.get('day'))){
        urgency = 'day';
    }
    else if(deadline > (ldt.getTime() + timeUnits.get('day')) &&
        deadline < (ldt.getTime() + timeUnits.get('week'))){
        urgency = 'week';
    }
    else if(deadline > (ldt.getTime() + timeUnits.get('week')) &&
        deadline < (ldt.getTime() + timeUnits.get('month'))){
        urgency = 'month';
    }
    else urgency = 'year';
    return urgency;
}

 function isUrgent(task){
        if($(task).attr('urgency') === 'hour' || $(task).attr('urgency') === 'day' ||
            $(task).attr('urgency') === 'yesterday'){
                    markUrgentTask(task);
        }
    }
//displaying all tasks
function displayAllTasks(){
    let tasks = $('.task-list').children('.task-item');
    for (let i = 0; i < tasks.length; i++){
        let task = tasks[i];
        isUrgent(task);
    }
}


//mark urgent tasks
function markUrgentTask(task){
    let name = $(task).children('.task-link').text();
    let urgency = $(task).attr('urgency');
    if(urgency === 'yesterday' ){
        name = addUrgencyToOverdueTask(name);
    }
    else if(urgency === 'hour' || urgency === 'day'){
        name = addUrgencyToApproachingDeadlineTask(name);
    }
    else {
        name = removeUrgencyFromNonUrgentTask(name);
    }
    $(task).children('.task-link').text(name);
}

function addUrgencyAttributeToTasks(){
    let tasks = $('.task-list').children('.task-item');
    for(let i = 0; i < tasks.length; i++){
        let task = tasks[i];
        let urgency = calculateUrgencyForTasks(task);
        $(task).attr('urgency', urgency);
    }
}


function markFiltersWithActiveTasks(){
    let tasks2urgency = new Map();
    let tasks = $('.task-list').children('.task-item');
    for(let i = 0; i < tasks.length; i++){
        let urgency = $(tasks[i]).attr('urgency');
        if (tasks2urgency.has(urgency)){
            let val = tasks2urgency.get(urgency);
            val++;
            tasks2urgency.set(urgency, val);
        } else {
            tasks2urgency.set(urgency, 1);
        }
    }

    let filters = $('.urgency-buttons').children('.filter-by-urgency');
    for(let i = 0; i < filters.length; i++){
        let filter_data = $(filters[i]).data('urgency');
        if(tasks2urgency.get(filter_data) > 0){
            let text = $(filters[i]).text();
            $(filters[i]).text(text + '*');
        } else continue;
    }
}


//addUrgencyToOverdueTask
function addUrgencyToOverdueTask(name){
    if(!name.endsWith(' Просрочен')){
        if(name.endsWith(' Срочно!')){
            name = name.replace(' Срочно!', ' Просрочен');
        } else name += ' Просрочен'
    } return name;
}

//addUrgencyToApproachingDeadlineTask
function addUrgencyToApproachingDeadlineTask(name){
    if(name.endsWith(' Срочно!'))return name;
    else if(name.endsWith(' Просрочен')){
        name = name.replace(' Просрочен', ' Срочно!');
    } else {
        name += ' Срочно!';
    }
    return name;
}

//removeUrgencyFromNonUrgentTask
function removeUrgencyFromNonUrgentTask(name){
    if(name.endsWith(' Срочно!')){
        name = name.replace(' Срочно!', '');

    }
    else if(name.endsWith(' Просрочен')){
        name = name.replace(' Просрочен', '');
    } return name;
}

//marking correspondent filter active on click on task link
function enableCorrespondingFilter(task){
    let taskUrgency = calculateUrgencyForTasks(task);
    if($('#all').hasClass('chosen')){
        let filters = $('.urgency-buttons').children('.filter-by-urgency');
        for(let i = 0; i < filters.length; i++){
            let filterUrgency = $(filters[i]).data('urgency');
            if(taskUrgency === filterUrgency){
                $(filters[i]).addClass('chosen');
                $('#all').removeClass('chosen');
                filterTasks(filters[i], taskUrgency);
                break;
            }
        }
    }
}


  //filter tasks
function filterTasks(filter, urgency){
    let tasks = $('.task-list').children('.task-item');
    for(let i = 0; i <  tasks.length; i++){
        let task = tasks[i];
        let urgency_value = $(task).attr('urgency');
        if(urgency_value !== urgency){
            $(task).hide();
        } else{
            $(task).show();
            let name = $(task).children('.task-link').text();
            $(task).css({'font-size': '22px'});
            name = removeUrgencyFromNonUrgentTask(name);
            $(task).children('.task-link').text(name);

        }
    }
}