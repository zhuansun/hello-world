package com.zspc.hw.manage.remote.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "sp", url = "${remote.sp.url:test}")
public interface SpFeign {

//    @PostMapping("/fns-sp/courierStation/stationVoById")
//    Result<RemoteBox> getBoxByCode(@RequestBody RemoteBoxSo so);


}
