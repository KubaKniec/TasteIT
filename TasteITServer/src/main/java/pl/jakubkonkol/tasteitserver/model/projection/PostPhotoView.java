package pl.jakubkonkol.tasteitserver.model.projection;

import pl.jakubkonkol.tasteitserver.model.PostMedia;

public interface PostPhotoView {
    String getPostId();
    PostMedia getPostMedia();
}
