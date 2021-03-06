package edu.ut.grouper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ut.grouper.bean.TransferBean;
import edu.ut.grouper.bean.UserBean;
import edu.ut.grouper.controller.common.ControllerTemplate;
import edu.ut.grouper.controller.common.ErrorCode;
import edu.ut.grouper.service.TransferManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/transfer")
public class TransferController extends ControllerTemplate {

    @RequestMapping(value = "/put", method = RequestMethod.POST)
    public ResponseEntity putShare(@RequestParam String shares, HttpServletRequest request) {
        String key = request.getHeader("key");
        final int succuess = transferManager.putShares(key, shares);
        if (succuess == -1) {
            return generateBadRequest(ErrorCode.ErrorAccessKey);
        }
        return generateOK(new HashMap<String, Object>() {{
            put("success", succuess);
        }});
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity getShareList(HttpServletRequest request) {
        String key = request.getHeader("key");
        final List<String> ids = transferManager.listShare(key);
        if (ids == null) {
            return generateBadRequest(ErrorCode.ErrorAccessKey);
        }
        return generateOK(new HashMap<String, Object>() {{
            put("shares", ids);
        }});
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity getShareContent(@RequestParam final List<String> id, HttpServletRequest request) {
        String key = request.getHeader("key");
        UserBean user = userManager.authByAccessKey(key);
        if (user == null) {
            return generateBadRequest(ErrorCode.ErrorAccessKey);
        }

        final List<Map> contents = new ArrayList<Map>();
        for (String tid : id) {

            Map<String, Object> content = new HashMap<String, Object>();
            content.put("id", tid);
            TransferBean transfer = transferManager.getShareContent(tid);
            if (transfer == null) {
                content.put("result", "notFound");
                content.put("data", null);
            } else if (transfer.getReceiver() != null && !transfer.getReceiver().equals(user.getUid())) {
                content.put("result", "noPrivilege");
                content.put("data", null);
            } else {
                content.put("result", "found");
                if (transfer.getReceiver() == null) {
                    transfer.setReceiver("*");
                }
                content.put("data", transfer);
            }

            contents.add(content);
        }

        return generateOK(new HashMap<String, Object>() {{
            put("contents", contents);
        }});
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseEntity confirmMessageShare(@RequestParam final List<String> messageId, HttpServletRequest request) {
        final List<String> notExistedMessageIds = transferManager.notExsitedMessageIds(messageId);
        return generateOK(new HashMap<String, Object>() {{
            put("messageIds", notExistedMessageIds);
        }});
    }

}