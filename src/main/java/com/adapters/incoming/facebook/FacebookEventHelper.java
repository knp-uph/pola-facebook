package com.adapters.incoming.facebook;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FacebookEventHelper {
    final Set<String> likeUrlList;

    public FacebookEventHelper(Set<String> likeUrlSet) {
        this.likeUrlList = likeUrlSet;
    }

    //TODO: de-snowflake?
    //TODO: DI-enable?
    public FacebookEventHelper() {
        likeUrlList = new HashSet<>();
        likeUrlList.add("https://scontent.xx.fbcdn.net/v/t39.1997-6/851557_369239266556155_759568595_n.png?_nc_ad=z-m&_nc_cid=0&oh=8bfd127ce3a4ae8c53f87b0e29eb6de5&oe=5A761DDC");
        likeUrlList.add("https://scontent.xx.fbcdn.net/v/t39.1997-6/p100x100/851587_369239346556147_162929011_n.png?_nc_ad=z-m&_nc_cid=0&oh=e01d19872a5c793a6e7ea989f0355fcd&oe=5A7D0FB0");
        likeUrlList.add("https://scontent.xx.fbcdn.net/v/t39.1997-6/p100x100/851582_369239386556143_1497813874_n.png?_nc_ad=z-m&_nc_cid=0&oh=b0600a6339b9808a8cd535bfeaad2547&oe=5AAF6B4D");
    }

    @Deprecated
    public boolean isLike(AttachmentMessageEvent event) {
        final List<AttachmentMessageEvent.Attachment> attachments = event.getAttachments();

        if (attachments.size() != 1) {
            return false;
        }
        final AttachmentMessageEvent.Attachment attachment = event.getAttachments().get(0);

        final AttachmentMessageEvent.Payload payload = attachment.getPayload();

        if (payload.isBinaryPayload()) {
            if (likeUrlList.contains(payload.asBinaryPayload().getUrl())) {
                return true;
            }
            return false;
        }

        return false;
    }
}
