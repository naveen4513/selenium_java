function dynamicEntityListFilterParser(){
    var all_filters =
        UI.execute(
            function(){
                var dynamic_xpath = [];
                var temp3=[];
                var list_item = {};
                var len = document.getElementsByClassName('jx-listFilters').length;

                for(var ctr=0;ctr<len;ctr++){
                    var temp = document.getElementsByClassName('jx-listFilters')[ctr];
                    list_item = {};
                    list_item ["Name of filter"]= temp.childNodes[0].innerText;
                    if(temp.childElementCount == 2){
                        var temp2 = temp.childNodes[1].innerText;
                        if(temp2 == "Select "){

                            list_item ["locater_button_filter"]= "//*[@id='filter']/ul/li['+(ctr+1)+']/div/div/button";
                            list_item ["filter_input_popup"]= "//*[@id='filter']/ul/li['+(ctr+1)+']/div/div/ul/li[1]/input";
                            list_item ["filter_type"]= "Mutiselect/Single select";
                            dynamic_xpath.push(list_item);
                        }
                        else{
                            list_item={};
                            list_item ["Name of filter"] = "";
                            dynamic_xpath.push(list_item);
                        }
                    }else if(temp.childElementCount==4){
                        var temp3=temp.childNodes[2].innerText.split("\n");
                        if(temp3[0]=="Date"){
                            list_item ["locater_button_filter"]= "//*[@id='filter']/ul/li['+(ctr+1)+']/div/div/button";
                            list_item ["filter_input_popup"]= "//*[@id='filter']/ul/li['+(ctr+1)+']/div/div/ul/li[1]/input";
                            list_item ["filter_type"]= "Mutiselect/Single select";
                            dynamic_xpath.push(list_item);
                        }
                        else{
                            list_item={};
                            list_item ["Name of filter"] = "";
                            dynamic_xpath.push(list_item);
                        }
                    }
                }
                return dynamic_xpath;
            });
    //TEST.log.info(JSON.stringify(all_filters));
    return all_filters;
}
/*Function to apply the filters */
function applyFilters(filter_name){
    I.click('//*[@id="preference"]/ul/li[1]/span[1]')
    var filters_list,index,array=0;
    var name=filter_name.split(";")
    I.wait(10)
    filters_list=dynamicEntityListFilterParser();



    for(var i=0;i<filters_list.length;i++){
        TEST.log.info(filter_list[i]);
        if(filter_list[i] == undefined){
            break;
        }
        else if(filters_list[i]==filter_name[i][0]){
            index = i;

        }
        continue;

    }


    UI.context('#preference',function(){
        I.wait(5)
        I.click(filters_list[index]["Name of filter"])
        I.click(filters_list[index]["locater_button_filter"])
        I.fill(filters_list[index]["filter_input_popup"],filter_name[index][1])
        I.click(filter_name[index][1])

        //I.click("Apply")
    })

}